// Generated code from Butter Knife. Do not modify!
package com.weathercheck;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MainActivity$$ViewInjector<T extends com.weathercheck.MainActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230723, "field 'time_label'");
    target.time_label = finder.castView(view, 2131230723, "field 'time_label'");
    view = finder.findRequiredView(source, 2131230721, "field 'temp_label'");
    target.temp_label = finder.castView(view, 2131230721, "field 'temp_label'");
    view = finder.findRequiredView(source, 2131230728, "field 'humid_value'");
    target.humid_value = finder.castView(view, 2131230728, "field 'humid_value'");
    view = finder.findRequiredView(source, 2131230730, "field 'precip_value'");
    target.precip_value = finder.castView(view, 2131230730, "field 'precip_value'");
    view = finder.findRequiredView(source, 2131230731, "field 'sum_label'");
    target.sum_label = finder.castView(view, 2131230731, "field 'sum_label'");
    view = finder.findRequiredView(source, 2131230725, "field 'weather_icon'");
    target.weather_icon = finder.castView(view, 2131230725, "field 'weather_icon'");
    view = finder.findRequiredView(source, 2131230732, "field 'refresh'");
    target.refresh = finder.castView(view, 2131230732, "field 'refresh'");
  }

  @Override public void reset(T target) {
    target.time_label = null;
    target.temp_label = null;
    target.humid_value = null;
    target.precip_value = null;
    target.sum_label = null;
    target.weather_icon = null;
    target.refresh = null;
  }
}
