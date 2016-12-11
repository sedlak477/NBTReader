# NBTReader
NBTReader is a tool to read and write NBT files.

NBTReader is still in development and may have bugs and/or be inefficient!

## Reading NBT Files

You can read NBT Files via the `NBTReader` class. It has three methods that return a `NBTTag`:
* fromFile(String filename)
* fromInputStream(InputStream in)
* fromGzippedFile(String filename)

```java
NBTTag tag = NBTReader.fromFile("./nbt_file");
```

## Writing NBT Files

You can write NBT Files via the `NBTWriter` class. It has three methods that write a `NBTTag`:
* writeToGzippedFile(NBTTag nbtTag, String filename)
* writeToFile(NBTTag nbtTag, String filename)
* writeToOutputStream(NBTTag nbtTag, OutputStream out)

```java
NBTTag tag = new NBTTag();
NBTReader.writeToFile(tag, "./nbt_file");  // You should catch the IOException!
```
