package reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;


public class NBTReader {

    private NBTTag root;

    public NBTReader(InputStream data) throws IOException {
        data.skip(1);
        root = new NBTTag(TagType.TagCompound, readString(data));
        root.setCompoundPayload(readCompound(data));
    }

    private NBTTag[] readCompound(InputStream in) throws InvalidNBTFormatException, IOException {
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

    private TagType readTagType(InputStream in) throws IOException {
        return TagType.values()[readByte(in)];
    }

    private byte readByte(InputStream in) throws IOException {
        return (byte) in.read();
    }

    private short readShort(InputStream in) throws IOException {
        return read(in, 2).getShort();
    }

    private int readUnsignedShort(InputStream in) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.put((byte)0);
        bb.put((byte)0);
        bb.put(readByte(in));
        bb.put(readByte(in));
        return bb.getInt(0);
    }

    private int readInt(InputStream in) throws IOException {
        return read(in, 4).getInt();
    }

    private long readLong(InputStream in) throws IOException {
        return read(in, 8).getLong();
    }

    private float readFloat(InputStream in) throws IOException {
        return read(in, 4).getFloat();
    }

    private double readDouble(InputStream in) throws IOException {
        return read(in, 8).getDouble();
    }

    private byte[] readByteArray(InputStream in) throws IOException {
        int length = readInt(in);
        return read(in, length).array();
    }

    private NBTTag[] readList(InputStream in) throws IOException {
        TagType type = readTagType(in);
        int length = readInt(in);
        NBTTag[] tags = new NBTTag[length];
        for (int i = 0; i < length; i++)
            tags[i] = readNBTTag(in, type);
        return tags;
    }

    private String readString(InputStream in) throws IOException {
        int length = readUnsignedShort(in);
        byte[] data = read(in, length).array();
        return new String(data, "UTF-8");
    }

    private int[] readIntArray(InputStream in) throws IOException {
        int length = readInt(in);
        return read(in, length).asIntBuffer().array();
    }

    private NBTTag readNBTTag(InputStream in, TagType type) throws IOException {
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

    private ByteBuffer read(InputStream in, int amount) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(amount);
        bb.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < amount; i++)
            bb.put(readByte(in));
        bb.position(0);
        return bb;
    }

    public NBTTag getRoot() {
        return root;
    }

    /**
     * Construct a NBTReader from a NBT gzipped file
     * @param filename
     * @return NBTReader
     * @throws IOException
     */
    public static NBTReader fromFile(String filename) throws IOException {
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(filename));
        return new NBTReader(in);
    }
}
