package pl.edu.dblach.przychodnia;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class NewAppointment_PagerAdapter extends FragmentStatePagerAdapter{
    public NewAppointment_PagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        switch (position){
            case 0: return new NewAppointment_tabClinic();
            case 1: return new NewAppointment_tabDoctor();
            case 2: return new NewAppointment_tabTime();
        }
        return null;
    }
    @Override public int getCount() {
        return 3;
    }

    @Override public CharSequence getPageTitle(int position) {
        switch (position){
            //case 0: return "Tab 1";
            //case 1: return "Tab 2";
            //case 2: return "Tab 3";
            default: return null;
        }
    }
}