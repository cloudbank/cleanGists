package com.teahouse.gists.ui;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.os.Bundle;

import com.teahouse.gists.viewmodel.GistViewModel;
import com.teahouse.gists.vo.Gist;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * using roboelectric instead of mockito to test lifecycle
 */
@RunWith(RobolectricTestRunner.class)
public class GistActivityCycleTest {
  ActivityController controller;
  GistViewModel gistViewModel;

  @Before
  public void setUp() {
    // Call the "buildActivity" method so we get an ActivityController which we can use
    // to have more control over the activity lifecycle
   // createdActivity = Robolectric.buildActivity(GistActivity.class).create().get();
   // controller = Robolectric.buildActivity(GistActivity.class);
    //need to reset the db to avoid the exception or use transaction
    //gistViewModel = mock(GistViewModel.class);

    //could use a spy for gists tests
   // when ()

  }

  @Test
  public void isStale() {
    GistActivity activity = Robolectric.setupActivity(GistActivity.class);
    activity.lastFetched = 0;
    assertThat(activity.isStale("test"), is(equalTo(true)));
  }

  @Test
  public void isNotStale() {
    GistActivity activity = Robolectric.setupActivity(GistActivity.class);
    activity.lastFetched = System.currentTimeMillis();
    assertThat(activity.isStale("test"), is(equalTo(false)));
  }


  @Test
  public void onCreateInit() {
    GistActivity activity = Robolectric.setupActivity(GistActivity.class);
    assertThat(activity.lastFetched, is(equalTo(0L)));
    assertThat(activity.gists, notNullValue());
    assertTrue(activity.gists.getValue().data.size() > 0);
    assertThat(activity.prefs.getLong("lastFetched", 0), is(equalTo(0L)));
  }

  @Test
  public void onStopResumeNotStale() {
    ActivityController controller = Robolectric.buildActivity(GistActivity.class).create().start().resume();

    controller.stop();

    GistActivity ga = (GistActivity)controller.start().resume().visible().get();
    assertTrue(ga.gists != null);
    assertTrue(ga.gists.getValue().data.size() > 0);


// assert it happened!
  }

  //stopped state to start
  //livedata should return cached not from db or network
  @Test
  public void onStopResumeStale() {
    ActivityController controller = Robolectric.buildActivity(GistActivity.class).create().start().resume();

    controller.stop();  //or pause

    GistActivity ga = (GistActivity)controller.get();
    List<Gist> gistsBefore = ga.gists.getValue().data;
    assertThat(ga.gists.getValue().data, notNullValue());

    ga = (GistActivity)controller.start().resume().get();
    ga.lastFetched = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10);
    assertTrue(System.currentTimeMillis() - ga.lastFetched >= TimeUnit.MINUTES.toMillis(10));
    assertThat(ga.isStale("test"), is(true));
    assertThat(ga.gists, not(gistsBefore));


  }

  //stopped state to start
  //vm should get from network if stale
  @Test
  public void onStartAfterStopStale() {
  }

  // reclaimed  with bundle timestamp for staleness
  //vm should get from network if stale
  @Test
  public void onCreateReclaimedNotStale() {
    Bundle savedInstanceState = new Bundle();
    //reclaimed before 10 mins
    long time = System.currentTimeMillis();
    savedInstanceState.putLong("lastFetched",time );

    GistActivity activity = Robolectric.buildActivity(GistActivity.class)
    .create(savedInstanceState).get();


    assertThat(savedInstanceState, notNullValue());
    assertThat(savedInstanceState.get("lastFetched"), is(equalTo(time)));
    assertThat(activity.lastFetched, is(equalTo(time)));
    assertThat(activity.isStale("test"), is(equalTo(false)));
  }

  @Test
  public void onCreateReclaimedStale() {
    Bundle savedInstanceState = new Bundle();
    //reclaimed before 10 mins
    savedInstanceState.putLong("lastFetched", 0);
    GistActivity activity = Robolectric.buildActivity(GistActivity.class)
        .create(savedInstanceState)
        .get();
    assertThat(savedInstanceState, notNullValue());
    assertThat(activity.isStale("test"), is(equalTo(true)));
  }

  @Test
  public void onStartGistsHasObserver() {
    Bundle savedInstanceState = new Bundle();
    //reclaimed before 10 mins
    savedInstanceState.putLong("lastFetched", 0);
    GistActivity activity = Robolectric.buildActivity(GistActivity.class)
        .create().start().get();
    assertThat(activity.gists.hasActiveObservers(), is(true));
  }

  @Test
  public void onConfigChangeStale() {
    //assertNull(activity.
    Bundle savedInstanceState = new Bundle();
    //reclaimed before 10 mins
    savedInstanceState.putLong("lastFetched", 0);
    ActivityController ac = Robolectric.buildActivity(GistActivity.class);

    controller.configurationChange();
    GistActivity activity = (GistActivity) ac
        .create(savedInstanceState)
        .get();
    assertThat(savedInstanceState, notNullValue());
    assertThat(activity.isStale("test"), is(equalTo(true)));
  }

  @Test
  public void onConfigChangeNotStale() {
    Bundle savedInstanceState = new Bundle();
    //reclaimed before 10 mins
    savedInstanceState.putLong("lastFetched", System.currentTimeMillis());
    GistActivity activity = Robolectric.buildActivity(GistActivity.class)
        .configurationChange()
        .create(savedInstanceState)
        .get();
    assertThat(savedInstanceState, notNullValue());
    assertThat(activity.isStale("test"), is(equalTo(false)));
  }
}
