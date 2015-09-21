package com.perezjuanjose.movip2.data;


import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface VideosColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey  @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String REF_ID_FILM = "refIdFilm";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String ISO_639_1 = "iso_639_1";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String KEY = "key";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SITE = "site";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String SIZE = "size";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TYPE = "type";

}
