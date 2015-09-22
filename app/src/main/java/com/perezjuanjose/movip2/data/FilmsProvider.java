package com.perezjuanjose.movip2.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by perez.juan.jose on 20/09/2015.
 */

@ContentProvider(
        authority = FilmsProvider.AUTHORITY,
        database = FilmDataBase.class

)
public class FilmsProvider {
    public static final String AUTHORITY = "com.perezjuanjose.movip2.data.FilmsProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String FILMS = "films";
        String VIDEOS = "videos";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = FilmDataBase.FILMS) public static class Films{
        @ContentUri(
                path = Path.FILMS,
                type = "vnd.android.cursor.dir/films",
                defaultSort = FilmsColumns._ID + " ASC")
               // defaultSort = PlanetColumns.DIST_FROM_SUN + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FILMS);

        @InexactContentUri(
                name = "FILM_ID",
                path = Path.FILMS + "/#",
                type = "vnd.android.cursor.item/films",
                whereColumn = FilmsColumns.COLUMN_FILM_ID,
                pathSegment = 1)
        public static Uri filmsByWEBId(long id){
            return buildUri(Path.FILMS, String.valueOf(id));
        }
    }

    @TableEndpoint(table = FilmDataBase.FILMS) public static class Videos{
        @ContentUri(
                path = Path.VIDEOS,
                type = "vnd.android.cursor.dir/videos",
                defaultSort = VideosColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.VIDEOS);

        @InexactContentUri(
                name = "VIDEO_ID",
                path = Path.VIDEOS + "/#",
                type = "vnd.android.cursor.item/videos",
                whereColumn = VideosColumns._ID,
                pathSegment = 1)
        public static Uri videoByWEBId(long id){
            return buildUri(Path.FILMS, String.valueOf(id));
        }
    }
}
