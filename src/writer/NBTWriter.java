package writer;

import nbt.NBTTag;
import nbt.TagType;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPOutputStream;

public class NBTWriter {

    private NBTWriter() {
    }

    /**
     * Write NBT into OutputStream
     * @param nbtTag NBT to write, must be of type COMPOUND
     * @param out OutputStream
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public static void writeToOutputStream(NBTTag nbtTag, OutputStream out) throws IllegalArgumentException, IOException {
        if (nbtTag.getType() != TagType.TagCompound)
            throw new IllegalArgumentException("Root element must be of type COMPOUND");
        writeTagType(out, TagType.TagCompound);
        writeCompound(out, nbtTag.getCompoundPayload());
    }

    /**
     * Write NBT into file
     * @param nbtTag NBT to write, must be of type COMPOUND
     * @param filename
     * @throws IOException
     */
    public static void writeToFile(NBTTag nbtTag, String filename) throws IOException {
        writeToOutputStream(nbtTag, new FileOutputStream(filename));
    }

    /**
     * Write NBT into file
     * @param nbtTag NBT to write, must be of type COMPOUND
     * @param filename
     * @throws IOException
     */
    public static void writeToGzippedFile(NBTTag nbtTag, String filename) throws IOException {
        writeToOutputStream(nbtTag, new GZIPOutputStream(new FileOutputStream(filename)));
    }

    private static void writeCompound(OutputStream out, NBTTag[] tag) throws IOException {
        for (NBTTag t : tag) {
            writeNamedNBTTag(out, t);
        }
        writeTagType(out, TagType.TagEnd);
    }

    private static void writeTagType(OutputStream out, TagType type) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put((byte) type.ordinal());
        write(out, bb);
    }

    private static void writeByte(OutputStream out, byte b) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(b);
        write(out, bb);
    }

    private static void writeShort(OutputStream out, short s) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort(s);
        write(out, bb);
    }

    private static void writeUnsignedShort(OutputStream out, int s) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort((short) s);
        write(out, bb);
    }

    private static void writeInt(OutputStream out, int i) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        write(out, bb);
    }

    private static void writeLong(OutputStream out, long l) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(l);
        write(out, bb);
    }

    private static void writeFloat(OutputStream out, float f) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putFloat(f);
        write(out, bb);
    }

    private static void writeDouble(OutputStream out, double d) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putDouble(d);
        write(out, bb);
    }

    private static void writeByteArray(OutputStream out, byte[] a) throws IOException {
        writeInt(out, a.length);
        out.write(a);
    }

    private static void writeList(OutputStream out, NBTTag[] l) throws IOException {
        writeTagType(out, l[0].getType());
        writeInt(out, l.length);
        for (NBTTag t : l)
            writeNBTTag(out, t);
    }

    private static void writeString(OutputStream out, String s) throws IOException {
        byte[] bytes = s.getBytes("UTF-8");
        writeUnsignedShort(out, bytes.length);
        out.write(bytes);
    }

    private static void writeIntArray(OutputStream out, int[] a) throws IOException {
        writeInt(out, a.length);
        ByteBuffer bb = ByteBuffer.allocate(a.length*4);
        for (int i : a)
            bb.putInt(i);
        write(out, bb);
    }

    private static void writeNamedNBTTag(OutputStream out, NBTTag tag) throws IOException {
        writeString(out, tag.getName());
        writeNBTTag(out, tag);
    }

    private static void writeNBTTag(OutputStream out, NBTTag tag) throws IOException {
        writeTagType(out, tag.getType());
        switch (tag.getType()){
            case TagByte:
                writeByte(out, tag.getBytePayload());
                break;
            case TagShort:
                writeShort(out, tag.getShortPayload());
                break;
            case TagInt:
                writeInt(out, tag.getIntPayload());
                break;
            case TagLong:
                writeLong(out, tag.getLongPayload());
                break;
            case TagFloat:
                writeFloat(out, tag.getFloatPayload());
                break;
            case TagDouble:
                writeDouble(out, tag.getDoublePayload());
                break;
            case TagString:
                writeString(out, tag.getStringPayload());
                break;
            case TagByteArray:
                writeByteArray(out, tag.getByteArrayPayload());
                break;
            case TagList:
                writeList(out, tag.getListPayload());
                break;
            case TagCompound:
                writeCompound(out, tag.getCompoundPayload());
                break;
            case TagIntArray:
                writeIntArray(out, tag.getIntArrayPayload());
                break;
        }
    }

    private static void write(OutputStream out, ByteBuffer bb) throws IOException {
        out.write(bb.array());
    }

}
