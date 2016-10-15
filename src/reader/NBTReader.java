package reader;

import exception.InvalidNBTFormatException;
import nbt.NBTTag;
import nbt.TagType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;


public class NBTReader {

    private NBTReader(){
    }

    private static NBTTag[] readCompound(InputStream in) throws InvalidNBTFormatException, IOException {
        ArrayList<NBTTag> tags = new ArrayList<>();
        TagType type;
        while (in.available() > 0 && (type = readTagType(in)) != TagType.TagEnd) {
            String name = readString(in);
            NBTTag tag = readNBTTag(in, type);
            tag.setName(name);
            tags.add(tag);
        }
        NBTTag[] a = new NBTTag[tags.size()];
        tags.toArray(a);
        return a;
    }

    private static TagType readTagType(InputStream in) throws IOException {
        return TagType.values()[readByte(in)];
    }

    private static byte readByte(InputStream in) throws IOException {
        return (byte) in.read();
    }

    private static short readShort(InputStream in) throws IOException {
        return read(in, 2).getShort();
    }

    private static int readUnsignedShort(InputStream in) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put((byte)0);
        bb.put((byte)0);
        bb.put(readByte(in));
        bb.put(readByte(in));
        return bb.getInt(0);
    }

    private static int readInt(InputStream in) throws IOException {
        return read(in, 4).getInt();
    }

    private static long readLong(InputStream in) throws IOException {
        return read(in, 8).getLong();
    }

    private static float readFloat(InputStream in) throws IOException {
        return read(in, 4).getFloat();
    }

    private static double readDouble(InputStream in) throws IOException {
        return read(in, 8).getDouble();
    }

    private static byte[] readByteArray(InputStream in) throws IOException {
        int length = readInt(in);
        return read(in, length).array();
    }

    private static NBTTag[] readList(InputStream in) throws IOException {
        TagType type = readTagType(in);
        int length = readInt(in);
        NBTTag[] tags = new NBTTag[length];
        for (int i = 0; i < length; i++)
            tags[i] = readNBTTag(in, type);
        return tags;
    }

    private static String readString(InputStream in) throws IOException {
        int length = readUnsignedShort(in);
        byte[] data = read(in, length).array();
        return new String(data, "UTF-8");
    }

    private static int[] readIntArray(InputStream in) throws IOException {
        int length = readInt(in);
        return read(in, length).asIntBuffer().array();
    }

    private static NBTTag readNBTTag(InputStream in, TagType type) throws IOException {
        NBTTag tag = new NBTTag(type);
        switch (type){
            case TagByte:
                tag.setBytePayload(readByte(in));
                break;
            case TagShort:
                tag.setShortPayload(readShort(in));
                break;
            case TagInt:
                tag.setIntPayload(readInt(in));
                break;
            case TagLong:
                tag.setLongPayload(readLong(in));
                break;
            case TagFloat:
                tag.setFloatPayload(readFloat(in));
                break;
            case TagDouble:
                tag.setDoublePayload(readDouble(in));
                break;
            case TagString:
                tag.setStringPayload(readString(in));
                break;
            case TagByteArray:
                tag.setByteArrayPayload(readByteArray(in));
                break;
            case TagList:
                tag.setListPayload(readList(in));
                break;
            case TagCompound:
                tag.setCompoundPayload(readCompound(in));
                break;
            case TagIntArray:
                tag.setIntArrayPayload(readIntArray(in));
                break;
        }
        return tag;
    }

    private static ByteBuffer read(InputStream in, int amount) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(amount);
        bb.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < amount; i++)
            bb.put(readByte(in));
        bb.position(0);
        return bb;
    }

    /**
     * Get NBT from gzipped file
     * @param filename
     * @return NBT root element
     * @throws IOException
     */
    public static NBTTag fromGzippedFile(String filename) throws IOException {
        return fromInputStream(new GZIPInputStream(new FileInputStream(filename)));
    }

    /**
     * Get NBT from file
     * @param filename
     * @return NBT root element
     * @throws IOException
     */
    public static NBTTag fromFile(String filename) throws IOException {
        return fromInputStream(new FileInputStream(filename));
    }

    /**
     * Get NBT from InputStream
     * @param in InputStream
     * @return NBT root element
     * @throws IOException
     */
    public static NBTTag fromInputStream(InputStream in) throws IOException {
        if (readTagType(in) != TagType.TagCompound)
            throw new InvalidNBTFormatException("Invalid root element");
        NBTTag root = new NBTTag(TagType.TagCompound, readString(in));
        root.setCompoundPayload(readCompound(in));
        return root;
    }
}
