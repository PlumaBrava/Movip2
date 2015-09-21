package com.perezjuanjose.movip2.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by perez.juan.jose on 20/09/2015.
 */

@Database(version = FilmDataBase.VERSION

    )
public class FilmDataBase {

    private FilmDataBase(){}

    public static final int VERSION = 1;


    @Table(FilmsColumns.class) public static final String FILMS = "films";

    @Table(VideosColumns.class) public static final String VIDEOS = "videos";


}
