// Generated code from Butter Knife. Do not modify!
package com.weathercheck;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class TestHttpActivity$$ViewInjector<T extends com.weathercheck.TestHttpActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230733, "field 'vid_viewer'");
    target.vid_viewer = finder.castView(view, 2131230733, "field 'vid_viewer'");
  }

  @Override public void reset(T target) {
    target.vid_viewer = null;
  }
}
