package net.minestom.server.data.type;

import net.minestom.server.data.DataType;
import net.minestom.server.utils.binary.BinaryBuffer;
import net.minestom.server.utils.binary.BinaryWriter;
import org.jetbrains.annotations.NotNull;

public class ShortData extends DataType<Short> {

    @Override
    public void encode(@NotNull BinaryWriter writer, @NotNull Short value) {
        writer.writeShort(value);
    }

    @NotNull
    @Override
    public Short decode(@NotNull BinaryBuffer reader) {
        return reader.readShort();
    }
}
