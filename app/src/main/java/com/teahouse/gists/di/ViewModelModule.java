package com.teahouse.gists.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.teahouse.gists.viewmodel.GistViewModel;
import com.teahouse.gists.viewmodel.GithubViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
  //the reason we need to inject this is because the constructor is not empty
  //the default factory only works with default constructor
  @Binds
  @IntoMap
  @ViewModelKey(GistViewModel.class)
  abstract ViewModel bindGistViewModel(GistViewModel gistViewModel);

  @Binds
  abstract ViewModelProvider.Factory bindViewModelFactory(GithubViewModelFactory factory);
}
