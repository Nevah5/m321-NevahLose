const playerServiceUrl = import.meta.env.VITE_PLAYER_SERVICE_URL;

export async function getSelf(token: string) {
  const response = await fetch(`${playerServiceUrl}/self`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.json();
}

export async function getRandomUsername() {
  const response = await fetch(`${playerServiceUrl}/usernames/random`);
  return response.text();
}
