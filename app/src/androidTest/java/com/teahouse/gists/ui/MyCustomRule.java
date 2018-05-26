package com.teahouse.gists.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;

import com.teahouse.gists.resource.Resource;
import com.teahouse.gists.viewmodel.GistViewModel;
import com.teahouse.gists.vo.Gist;

import java.util.List;

public class MyCustomRule<A extends GistActivity> extends ActivityTestRule<A> {
  public MyCustomRule(Class<A> activityClass, boolean touchMode, boolean launch) {
    super(activityClass, touchMode, launch);
  }
  protected GistViewModel viewModel;
  protected MutableLiveData<Resource<List<Gist>>> gistsData = new MutableLiveData<>();

  @Override
  protected void beforeActivityLaunched() {
    super.beforeActivityLaunched();
    viewModel = mock(GistViewModel.class);
    when(viewModel.getGists(true)).thenReturn(gistsData);
    // Maybe prepare some mock service calls
    // Maybe override some depency injection modules with mocks
  }



  @Override
  protected void afterActivityLaunched() {
    super.afterActivityLaunched();
    // maybe you want to do something here
  }


}
