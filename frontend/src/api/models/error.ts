export default interface ErrorResponse {
  code: number;
  message: string;
}

export interface ServerErrorResponse extends ErrorResponse {
  errorCode: string;
}
