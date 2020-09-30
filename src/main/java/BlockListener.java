import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import com.sun.jna.platform.win32.Wtsapi32;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;


public class BlockListener  implements WindowProc {

    HWND hWnd;
    String windowClass ;
    HMODULE hInst;
    private boolean isBlocked;
    ICallBackVoid callBackVoid;


    protected HWND createWindow(String windowClass, HMODULE hInst){

        WNDCLASSEX wClass = new WNDCLASSEX();
        wClass.hInstance = hInst;
        wClass.lpfnWndProc = BlockListener.this;
        wClass.lpszClassName = windowClass;

        User32.INSTANCE.RegisterClassEx(wClass);

        HWND hWnd = User32.INSTANCE
                .CreateWindowEx(
                        User32.WS_EX_TOPMOST,
                        windowClass,
                        "My hidden helper window, used only to catch the windows events",
                        0, 0, 0, 0, 0,
                        null, // WM_DEVICECHANGE contradicts parent=WinUser.HWND_MESSAGE
                        null, hInst, null);
        return hWnd;
    }

    public BlockListener(){
        windowClass = "MyWindowClass";
        hInst = Kernel32.INSTANCE.GetModuleHandle("");
        this.hWnd = this.createWindow(windowClass,hInst);
        Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hWnd,
                Wtsapi32.NOTIFY_FOR_THIS_SESSION);

        //this.listen();
    }


    public void listen()  {
        MSG msg = new MSG();
        User32.INSTANCE.GetMessage(msg, hWnd, 0, 0);
        User32.INSTANCE.TranslateMessage(msg);
        User32.INSTANCE.DispatchMessage(msg);
        //System.out.println(isBlocked);
    }

    public boolean listenLoop() {
        long workCounter=0L;
        long limit=30;
        int delayTime=1;
        long threadDelay=delayTime*1000;
        MSG msg = new MSG();
        while (workCounter<limit) {
            User32.INSTANCE.GetMessage(msg, hWnd, 0, 0);
            User32.INSTANCE.TranslateMessage(msg);
            User32.INSTANCE.DispatchMessage(msg);
/*
            workCounter += delayTime;
            Thread.sleep(threadDelay);
*/
            System.out.println("listen cicle");
        }
        System.out.println("stop");

        destroy();
        return isBlocked;
    }

    public void destroy() {
        Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hWnd);
        User32.INSTANCE.UnregisterClass(windowClass, hInst);
        User32.INSTANCE.DestroyWindow(hWnd);
    }

    public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam) {
        switch (uMsg) {
            case WinUser.WM_DESTROY: {
                User32.INSTANCE.PostQuitMessage(0);
                return new LRESULT(0);
            }
            case WinUser.WM_SESSION_CHANGE: {
                this.onSessionChange(wParam, lParam);
                return new LRESULT(0);
            }
            default:
                return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
        }
    }

    protected void onSessionChange(WPARAM wParam, LPARAM lParam) {
        switch (wParam.intValue()) {
            case Wtsapi32.WTS_SESSION_LOCK: {
                System.out.println("locked");
                isBlocked=true;
                if (callBackVoid!=null)
                    callBackVoid.call(true);
                break;
            }
            case Wtsapi32.WTS_SESSION_UNLOCK: {
                isBlocked=false;
                if (callBackVoid!=null) callBackVoid.call(false);
                System.out.println("unlocked");
                break;
            }
        }
    }

    public ICallBackVoid getCallBackVoid() {
        return callBackVoid;
    }

    public void setCallBackVoid(ICallBackVoid callBackVoid) {
        this.callBackVoid = callBackVoid;
    }

    public static void main(String[] args) throws InterruptedException {
        BlockListener l=new BlockListener();
        l.listenLoop();
    }
}


