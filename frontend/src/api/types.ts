export type ApiError = {
  message: string;
  code: number;
  errorCode?: string;
};

export interface TokenResponse {
  token: string;
  type: string;
  expires_at: number;
}

export interface Player {
  id: string;
  username: string;
}
