class ToastApi {
  private readonly listeners: CallableFunction[] = [];

  emit(toast: Toast) {
    this.listeners.forEach((listener) => listener(toast));
  }

  subscribe(listener: CallableFunction) {
    this.listeners.push(listener);
  }
}

interface Toast {
  title: string;
  message: string;
  timeout?: number;
  type?: "error" | "info";
}

interface ToastTechnical extends Toast {
  id: string;
  timeoutUpdaterRef?: NodeJS.Timeout;
  timeoutStarted?: number;
}

export default new ToastApi();

export type { Toast, ToastTechnical };
