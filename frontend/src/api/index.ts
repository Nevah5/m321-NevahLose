import type { AxiosInstance, AxiosResponse } from "axios";
import type { Player, ApiError, TokenResponse, Card, Game } from "./types";
import SockJS from "sockjs-client";
import { Client, Stomp } from "@stomp/stompjs";

import axios from "axios";
import { handleApiError } from "./errorHandler";

class ApiService {
  public readonly axiosInstance: AxiosInstance;

  constructor(baseURL: string) {
    this.axiosInstance = axios.create({
      baseURL,
      headers: {
        "Content-Type": "application/json",
      },
    });
  }

  private handleResponse<T>(response: AxiosResponse<T>): T {
    return response.data;
  }

  private handleError(error: any): ApiError {
    return handleApiError(error);
  }

  public async get<T>(url: string, token?: string): Promise<T> {
    try {
      const config = token
        ? { headers: { Authorization: `Bearer ${token}` } }
        : {};
      const response = await this.axiosInstance.get<T>(url, config);
      return this.handleResponse(response);
    } catch (error) {
      throw this.handleError(error);
    }
  }

  public async post<T>(url: string, data?: any, token?: string): Promise<T> {
    try {
      const config = token
        ? { headers: { Authorization: `Bearer ${token}` } }
        : {};
      const response = await this.axiosInstance.post<T>(url, data, config);
      return this.handleResponse(response);
    } catch (error) {
      throw this.handleError(error);
    }
  }
}

class CardService extends ApiService {
  constructor(cardServiceUrl: string) {
    super(`${cardServiceUrl}`);
  }

  async getCards(): Promise<Card[]> {
    return this.get<Card[]>("/cards");
  }
}

class PlayerService extends ApiService {
  constructor(playerServiceUrl: string) {
    super(`${playerServiceUrl}`);
  }

  async getSelf(token: string): Promise<Player> {
    return this.get<Player>("/self", token);
  }

  async register(username: string): Promise<TokenResponse> {
    return this.post<TokenResponse>("/auth/register", { username });
  }

  async getRandomUsername(): Promise<string> {
    return this.get<string>("/usernames/random");
  }
}

class GameService extends ApiService {
  private stompClient: Client | null = null;

  constructor(gameServiceUrl: string) {
    super(`${gameServiceUrl}`);
  }

  async getGameFromRoomId(roomId: string, token: string): Promise<Game> {
    return this.get<Game>(`/games/rooms/${roomId}`, token);
  }

  async createGame(token: string): Promise<Game> {
    return this.post<Game>("/games/create", {}, token);
  }

  async joinGame(gameId: string, token: string): Promise<Client> {
    return new Promise<Client>((resolve, reject) => {
      // const websocketUrl = this.axiosInstance.defaults.baseURL!;
      const sockJS = new SockJS(`http://localhost:8082/stomp?token=${token}`);
      this.stompClient = new Client({
        webSocketFactory: () => sockJS,
        connectHeaders: {
          Authorization: `Bearer ${token}`,
        },
        onStompError: (frame) => {
          window.alert(frame.body);
          reject(new Error(frame.body));
        },
        debug: (str: string) => console.log(str),
        onDisconnect: () => console.log("Websocket Disconnected"),
        onWebSocketError: (event) => {
          window.alert("WebSocket error\n" + event);
          reject(new Error("WebSocket error"));
        },
        onWebSocketClose: (event) => console.log("WebSocket closed", event),
        onConnect: () => {
          console.log("Websocket Connected");
          resolve(this.stompClient!);
        },
      });

      this.stompClient.activate();
    });
  }

  async leaveGame() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
    }
  }
}

const playerService = new PlayerService(
  import.meta.env.VITE_PLAYER_SERVICE_URL
);
const cardService = new CardService(import.meta.env.VITE_CARD_SERVICE_URL);
const gameService = new GameService(import.meta.env.VITE_GAME_SERVICE_URL);

export { playerService, cardService, gameService };
