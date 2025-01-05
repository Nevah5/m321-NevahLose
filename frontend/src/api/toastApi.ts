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
}

interface ToastTechnical extends Toast {
  id: string;
  timeoutUpdaterRef?: number;
  timeoutStarted?: number;
}

export default new ToastApi();

export type { Toast, ToastTechnical };
