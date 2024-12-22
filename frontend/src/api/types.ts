export type ApiError = {
  message: string;
  code: number;
  errorCode?: string;
};

export interface TokenResponse {
  token: string;
  type: string;
  expires_in: number;
}

export interface Player {
  id: string;
  username: string;
}
