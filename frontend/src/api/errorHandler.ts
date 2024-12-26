import type { ApiError } from "./types";

export function handleApiError(error: any): ApiError {
  if (error.response) {
    const { status, data } = error.response;
    return {
      message: data.message || "An error occurred",
      code: status,
      errorCode: data.errorCode,
    };
  } else if (error.request) {
    return {
      message: "No response received from the server",
      code: 0,
    };
  } else {
    return {
      message: error.message || "An unexpected error occurred",
      code: 500,
    };
  }
}
