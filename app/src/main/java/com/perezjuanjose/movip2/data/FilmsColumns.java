package com.perezjuanjose.movip2.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by perez.juan.jose on 20/09/2015.
 */
public interface FilmsColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    // Number of film en the web database
    @DataType(DataType.Type.INTEGER) @NotNull
    public static final String COLUMN_FILM_ID = "id";

    // Film For adults
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_ADULTS = "adults";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_BACKDROP_PATH= "backdrop_path";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_ORIGINAL_LANGUAJE = "origianlLanguaje";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_RELEASE_DATE = "releaseDate";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_POSTER_PATH = "posterPath";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String COLUMN_POPULARITY = "popularity";


    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_TITLE = "title";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String COLUMN_VIDEO = "video";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String COLUMN_VOTE_AGERAGE = "voteAverage";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String COLUMN_VOTE_COUNT = "vote_count";
}




