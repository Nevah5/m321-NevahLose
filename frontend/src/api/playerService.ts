import type ErrorResponse from "./models/error";
import type { ServerErrorResponse } from "./models/error";
import type TokenResponse from "./models/token";
import type Player from "./models/user";

const playerServiceUrl = import.meta.env.VITE_PLAYER_SERVICE_URL;

export async function getSelf(token: string): Promise<Player> {
  const response = await fetch(`${playerServiceUrl}/self`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.json();
}

export async function getRandomUsername(): Promise<string> {
  const response = await fetch(`${playerServiceUrl}/usernames/random`);
  return response.text();
}

export async function register(username: string): Promise<TokenResponse> {
  return new Promise<TokenResponse>((resolve, reject) => {
    fetch(`${playerServiceUrl}/auth/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username }),
    })
      .then(async (response) => {
        const json = await response.json();
        if (response.ok) {
          resolve(json);
        } else if (response.status === 400) {
          reject(new Error((json as ErrorResponse).message));
        } else {
          reject(
            new Error(
              `Error code ${(json as ServerErrorResponse).errorCode}:\n${
                (json as ServerErrorResponse).message
              }`
            )
          );
        }
      })
      .catch((error) => {
        reject(new Error(error));
      });
  });
}
