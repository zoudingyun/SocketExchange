package per.zdy.socketexchangeclientcp.core;

import per.zdy.socketexchangeclientcp.web.ServerCenterWebSocketController;

import java.io.*;
import java.util.Arrays;

import static per.zdy.socketexchangeclientcp.share.PublicVariable.deleteSubString;

public class ConsoleStream extends ByteArrayOutputStream {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private PrintStream oldPrintStream;
    private PrintStream newPrintStream;

    public ConsoleStream(PrintStream oldPrintStream){
        this.oldPrintStream=oldPrintStream;
        this.newPrintStream = new PrintStream(this);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }


    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buf.length > 0){
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buf.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0){
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0){
            newCapacity = hugeCapacity(minCapacity);
        }

        buf = Arrays.copyOf(buf, newCapacity);
    }

    /**
     * Writes the specified byte to this byte array output stream.
     *
     * @param   b   the byte to be written.
     */
    @Override
    public synchronized void write(int b) {
        ensureCapacity(count + 1);
        buf[count] = (byte) b;
        count += 1;
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this byte array output stream.
     *
     * @param   b     the data.
     * @param   off   the start offset in the data.
     * @param   len   the number of bytes to write.
     */
    @Override
    public synchronized void write(byte b[], int off, int len) {
        String s = new String(b);
        try {
            String[]tmp = deleteSubString(s,"\u001B[2m");
                    tmp = deleteSubString(tmp[0],"\u001B[0;39m");
                    tmp = deleteSubString(tmp[0],"\u001B[31m");
                    tmp = deleteSubString(tmp[0],"\u001B[32m");
                    tmp = deleteSubString(tmp[0],"\u001B[35m");
                    tmp = deleteSubString(tmp[0],"\u001B[36m");

            ServerCenterWebSocketController.sendConsoleInfo(tmp[0]);
        }catch (Exception ex){

        }
        System.setOut(oldPrintStream);
        System.out.print(s);
        System.setOut(newPrintStream);
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(count + len);
        System.arraycopy(b, off, buf, count, len);
        count += len;
    }

}
