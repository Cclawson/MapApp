package com.example.mapapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> songList;
    private ListView songView;
    private static Cursor genresCursor;

    private static String[] genresProjection = {
            MediaStore.Audio.Genres.NAME,
            MediaStore.Audio.Genres._ID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songView = (ListView)findViewById(R.id.song_list);

        songList = new ArrayList<Song>();

        getSongList();
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                String info = "";
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                int musicId = Integer.parseInt(musicCursor.getString(idColumn));

                Uri uri = android.provider.MediaStore.Audio.Genres.getContentUriForAudioId("external", musicId);
                genresCursor = this.getApplicationContext().getContentResolver().query(uri,
                        genresProjection, null, null, null);
                int genre_column_index = genresCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);

                if (genresCursor.moveToFirst()) {
                    info += "Genres: ";
                    do {
                        info += genresCursor.getString(genre_column_index) + " ";
                    } while (genresCursor.moveToNext());
                }
                System.out.println(info);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }


    public void showMusicPlayer(View view) {
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }

    public void OnMainButtonClick(View view)
    {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

}
