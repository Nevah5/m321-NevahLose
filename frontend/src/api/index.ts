import type { AxiosInstance, AxiosResponse } from "axios";
import type { Player, ApiError } from "./types";

import axios from "axios";
import { handleApiError } from "./errorHandler";

class ApiService {
  private readonly axiosInstance: AxiosInstance;

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

  public async post<T>(url: string, data: any, token?: string): Promise<T> {
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

class PlayerService extends ApiService {
  constructor(playerServiceUrl: string) {
    super(`${playerServiceUrl}`);
  }

  async getSelf(token: string): Promise<Player> {
    return this.get<Player>("/self", token);
  }
}

const playerService = new PlayerService(
  import.meta.env.VITE_PLAYER_SERVICE_URL
);

export { playerService };
