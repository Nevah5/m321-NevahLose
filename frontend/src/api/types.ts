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

export interface GamePlayer {
  gameId: string;
  playerId: string;
  username: string;
  joinedAt: number;
  leftAt: number | null;
  host: boolean;
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

export interface Game {
  id: string;
  roomId: string;
  ownerId: string;
  gameStatus: string;
  maxPlayers: number;
  createdAt: number;
  startedAt: number;
  currentTurnId: string;
  winnerId: string;
}

export interface ChatMessage {
  id: string;
  gameId: string;
  senderId: string;
  senderUsername: string;
  type: "ACTIVITY" | "MESSAGE";
  joined?: boolean;
  message?: string;
  host: boolean;
}

export interface GameActivity {
  type:
    | "GAME_START"
    | "GAME_TERMINATE"
    | "CARD_PLAYED"
    | "CARD_DRAWN"
    | "NEXT_PLAYER";
  message?: string;
  cardId?: string;
  playerId?: string;
  gameId: string;
}
