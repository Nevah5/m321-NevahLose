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

export interface Card {
  id: string;
  name: string;
  description: string;
  backgroundFilename: string;
  subjectFilename: string;
  cardType: string;
  amountInDeck: number;
}
