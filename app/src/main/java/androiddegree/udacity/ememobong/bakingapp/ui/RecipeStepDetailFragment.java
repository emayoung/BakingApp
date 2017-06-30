package androiddegree.udacity.ememobong.bakingapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import androiddegree.udacity.ememobong.bakingapp.MainActivity;
import androiddegree.udacity.ememobong.bakingapp.R;
import androiddegree.udacity.ememobong.bakingapp.model.PreparationSteps;
import androiddegree.udacity.ememobong.bakingapp.model.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener{

    private SimpleExoPlayerView simpleExoPlayerView;
    TextView descriptionTextView;
    FloatingActionButton prevButton;
    FloatingActionButton nextButton;

    private SimpleExoPlayer player;
    private boolean shouldAutoPlay;
    private int playerWindow;
    private long playerPosition;

    private String videoUrl;
    private String imagerl;
    Recipe recipe;
    List<PreparationSteps> steps;
    int position = 0;
    public static final String POSITION = "position";
    boolean mTwoPane = true;
    public static final String TWO_PANE_BUNDLE_KEY = "is_two_pane";

    Context context;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();


    // Mandatory empty constructor
    public RecipeStepDetailFragment() {
    }

    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    // Inflates the GridView of all AndroidMe images
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_recipe_step_detail, container, false);
        descriptionTextView = (TextView) rootView.findViewById(R.id.description_textview);
        prevButton = (FloatingActionButton) rootView.findViewById(R.id.prev_button);
        nextButton = (FloatingActionButton) rootView.findViewById(R.id.next_button);
        recipe = getArguments().getParcelable(RecipeCardAdapter.PARCEABLE_RECIPE_KEY);
        position = getArguments().getInt(POSITION);
        mTwoPane = getArguments().getBoolean(TWO_PANE_BUNDLE_KEY);

        steps = recipe.getSteps();
        videoUrl = steps.get(position).getVideoURL();
        imagerl = steps.get(position).getThumbnailURL();

        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);

        ButterKnife.bind(this, rootView);
        descriptionTextView = (TextView) rootView.findViewById(R.id.description_textview);
        descriptionTextView.setText(steps.get(position).getDescription());

        initializeMediaSession();
        initializePlayer();

        if (!isNetworkAvailable()){
            if (prevButton != null){
                prevButton.setEnabled(false);
                nextButton.setEnabled(false);
            }

            Bitmap imageHolder = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.no_internet);
            simpleExoPlayerView.setDefaultArtwork(imageHolder);

        }
        if(prevButton != null  ){
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                decrement the position and replace the fragment
                    if(position != 0){
                        position--;
                        updateVideoAndDescription(position);
                    }
                }
            });
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                decrement the position and replace the fragment
                    if(position < recipe.getSteps().size()-1){
                        position++;
                        updateVideoAndDescription(position);
                    }
                }
            });
        }

        // Return the root view
        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initializePlayer() {
        if (player == null) {
            // a factory to create an AdaptiveVideoTrackSelection
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                    new DefaultLoadControl());

            simpleExoPlayerView.setPlayer(player);

            if (!videoUrl.isEmpty()){
                Uri uri = Uri.parse(videoUrl);

                MediaSource mediaSource = buildMediaSource(uri);
                player.prepare(mediaSource, true, false);
                player.setPlayWhenReady(true);
            }
            else if(!imagerl.isEmpty()){
                Uri uri = Uri.parse(imagerl);

                MediaSource mediaSource = buildMediaSource(uri);
                player.prepare(mediaSource, true, false);
                player.setPlayWhenReady(true);
            }
            else {
                Bitmap imageHolder = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_launcher);
                simpleExoPlayerView.setDefaultArtwork(imageHolder);
            }


        }

    }
    public void updateVideoAndDescription(int position){
        descriptionTextView.setText(steps.get(position).getDescription());
        videoUrl = steps.get(position).getVideoURL();
        imagerl = steps.get(position).getThumbnailURL();
        if (!videoUrl.isEmpty()){
            Uri uri = Uri.parse(videoUrl);

            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
            player.setPlayWhenReady(true);
        }
        else if(!imagerl.isEmpty()){
            Uri uri = Uri.parse(imagerl);

            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
            player.setPlayWhenReady(true);
        } else {
            Bitmap imageHolder = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_launcher);
            simpleExoPlayerView.setDefaultArtwork(imageHolder);
        }

    }
    private MediaSource buildMediaSource(Uri uri) {

            DefaultExtractorsFactory extractorSourceFactory = new DefaultExtractorsFactory();
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ua");

            ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

            // this return a single mediaSource object. i.e. no next, previous buttons to play next/prev media file
            return new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        }
        private void releasePlayer() {
            if (player != null) {
                shouldAutoPlay = player.getPlayWhenReady();
                playerWindow = player.getCurrentWindowIndex();
                playerPosition = C.TIME_UNSET;
                player.release();

            }
        }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    private void initializeMediaSession() {

             // Create a MediaSessionCompat.
             mMediaSession = new MediaSessionCompat(getActivity(), TAG);

             // Enable callbacks from MediaButtons and TransportControls.
             mMediaSession.setFlags(
                     MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                             MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            // Do not let MediaButtons restart the player when the app is not visible.
            mMediaSession.setMediaButtonReceiver(null);

         // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
           mStateBuilder = new PlaybackStateCompat.Builder()
                 .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                 PlaybackStateCompat.ACTION_PLAY_PAUSE);
         mMediaSession.setPlaybackState(mStateBuilder.build());
           // MySessionCallback has methods that handle callbacks from a media controller.
            mMediaSession.setCallback(new MySessionCallback());
          // Start the Media Session since the activity is active.
       mMediaSession.setActive(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            player.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }
    }

    //  from the documentation  hideSystemUi called in onResume is just an implementation detail to have a pure full screen experience
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


}
