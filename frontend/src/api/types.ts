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
  imageName: string;
  subjectName: string;
  cardType: string;
  amountInDeck: number;
}
