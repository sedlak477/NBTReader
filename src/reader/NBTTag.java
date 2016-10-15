package reader;


public class NBTTag {

    private TagType type;
    private String name;
    private byte bytePayload;
    private short shortPayload;
    private int intPayload;
    private long longPayload;
    private float floatPayload;
    private double doublePayload;
    private byte[] byteArrayPayload;
    private String stringPayload;
    private NBTTag[] listPayload;
    private NBTTag[] compoundPayload;
    private int[] intArrayPayload;

    /**
     * @param type
     * @param name
     */
    public NBTTag(TagType type, String name){
        this.type = type;
        this.name = name;
    }

    /**
     * @param type
     */
    public NBTTag(TagType type){
        this(type, null);
    }

    /**
     */
    public NBTTag(){
        this(null, null);
    }

    /**
     * Returns the payload that corresponds to the TagType
     * @return payload
     */
    public Object getPayload(){
        switch (type){
            case TagEnd:
                return null;
            case TagByte:
                return getBytePayload();
            case TagShort:
                return getShortPayload();
            case TagInt:
                return getIntPayload();
            case TagLong:
                return getLongPayload();
            case TagFloat:
                return getFloatPayload();
            case TagDouble:
                return getDoublePayload();
            case TagString:
                return getStringPayload();
            case TagByteArray:
                return getByteArrayPayload();
            case TagList:
                return getListPayload();
            case TagCompound:
                return getCompoundPayload();
            case TagIntArray:
                return getIntArrayPayload();
        }
        return null;
    }

    public boolean isNamed(){
        return name != null;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getBytePayload() {
        return bytePayload;
    }

    public void setBytePayload(byte bytePayload) {
        this.bytePayload = bytePayload;
    }

    public short getShortPayload() {
        return shortPayload;
    }

    public void setShortPayload(short shortPayload) {
        this.shortPayload = shortPayload;
    }

    public int getIntPayload() {
        return intPayload;
    }

    public void setIntPayload(int intPayload) {
        this.intPayload = intPayload;
    }

    public long getLongPayload() {
        return longPayload;
    }

    public void setLongPayload(long longPayload) {
        this.longPayload = longPayload;
    }

    public float getFloatPayload() {
        return floatPayload;
    }

    public void setFloatPayload(float floatPayload) {
        this.floatPayload = floatPayload;
    }

    public double getDoublePayload() {
        return doublePayload;
    }

    public void setDoublePayload(double doublePayload) {
        this.doublePayload = doublePayload;
    }

    public byte[] getByteArrayPayload() {
        return byteArrayPayload;
    }

    public void setByteArrayPayload(byte[] byteArrayPayload) {
        this.byteArrayPayload = byteArrayPayload;
    }

    public String getStringPayload() {
        return stringPayload;
    }

    public void setStringPayload(String stringPayload) {
        this.stringPayload = stringPayload;
    }

    public NBTTag[] getListPayload() {
        return listPayload;
    }

    public void setListPayload(NBTTag[] listPayload) {
        this.listPayload = listPayload;
    }

    public NBTTag[] getCompoundPayload() {
        return compoundPayload;
    }

    public void setCompoundPayload(NBTTag[] compoundPayload) {
        this.compoundPayload = compoundPayload;
    }

    public int[] getIntArrayPayload() {
        return intArrayPayload;
    }

    public void setIntArrayPayload(int[] intArrayPayload) {
        this.intArrayPayload = intArrayPayload;
    }

    private String toString(int indent) {
        final int indentIncrement = 2;
        StringBuilder sb = new StringBuilder();
        if (getPayload() instanceof NBTTag[]){
            sb.append(String.format("%s('%s') {\n", getType(), getName()));
            for (NBTTag o : (NBTTag[]) getPayload()) {
                for (int i = 0; i < indent; i++)
                    sb.append(" ");
                sb.append(o.toString(indent+indentIncrement));
                sb.append("\n");
            }
            for (int i = 0; i < indent-indentIncrement; i++)
                sb.append(" ");
            sb.append("}");
        } else
            sb.append(String.format("%s('%s'): %s", getType(), getName(), getPayload()));
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.toString(0);
    }
}
