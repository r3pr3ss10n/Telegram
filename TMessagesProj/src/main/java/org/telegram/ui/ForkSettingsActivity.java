package org.telegram.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

import java.util.ArrayList;

public class ForkSettingsActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    private int rowCount;
    private int sectionRow1;

    private int ghostRow;
    private int noTypingRow;
    private int noReadingRow;
    private int forceRearCamRow;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        rowCount = 0;
        sectionRow1 = rowCount++;
        ghostRow = rowCount++;
        noTypingRow = rowCount++;
        return true;
    }

    @Override
    public View createView(Context context) {
        setupActionBar(context);
        listAdapter = new ListAdapter(context);

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        fragmentView = frameLayout;

        setupRecyclerView(context, frameLayout);

        return fragmentView;
    }

    private void setupActionBar(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString("ForkSettingsTitle", R.string.ForkSettingsTitle));
        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });
    }

    private void setupRecyclerView(Context context, FrameLayout frameLayout) {
        listView = new RecyclerListView(context);
        listView.setVerticalScrollBarEnabled(false);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setGlowColor(Theme.getColor(Theme.key_avatar_backgroundActionBarBlue));
        listView.setAdapter(listAdapter);
        listView.setItemAnimator(null);
        listView.setLayoutAnimation(null);

        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.LEFT));

        listView.setOnItemClickListener((view, position, x, y) -> {
            if (position == ghostRow && view instanceof TextCheckCell) {
                SharedConfig.toggleGhost();
                ((TextCheckCell) view).setChecked(SharedConfig.ghost);
            }
            if (position == noTypingRow && view instanceof TextCheckCell) {
                SharedConfig.toggleNoTyping();
                ((TextCheckCell) view).setChecked(SharedConfig.noTyping);
            }
            if (position == noReadingRow && view instanceof TextCheckCell) {
                SharedConfig.toggleNoReading();
                ((TextCheckCell) view).setChecked(SharedConfig.noReading);
            }
            if (position == forceRearCamRow && view instanceof TextCheckCell) {
                SharedConfig.toggleForceRearCam();
                ((TextCheckCell) view).setChecked(SharedConfig.forceRearCam);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private final Context mContext;

        ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == ghostRow || position == noTypingRow || position == noReadingRow || position == forceRearCamRow) {
                return 3; // TextCheckCell
            } else if (position == sectionRow1) {
                return 4; // HeaderCell
            }
            return 6; // Default type for other cases
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = createViewForType(viewType);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        private View createViewForType(int viewType) {
            switch (viewType) {
                case 1:
                    return new ShadowSectionCell(mContext);
                case 3:
                    return createTextCheckCell();
                case 4:
                    return createHeaderCell();
                default:
                    return new View(mContext); // Placeholder for default cases
            }
        }

        private View createTextCheckCell() {
            TextCheckCell textCell = new TextCheckCell(mContext);
            textCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            return textCell;
        }

        private View createHeaderCell() {
            HeaderCell headerCell = new HeaderCell(mContext);
            headerCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            return headerCell;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == 3) { // TextCheckCell
                bindTextCheckCell(holder, position);
            } else if (holder.getItemViewType() == 4) { // HeaderCell
                bindHeaderCell(holder, position);
            }
        }

        private void bindTextCheckCell(RecyclerView.ViewHolder holder, int position) {
            TextCheckCell textCell = (TextCheckCell) holder.itemView;
            SharedPreferences preferences = MessagesController.getGlobalMainSettings();
            if (position == ghostRow) {
                String title = LocaleController.getString("GhostMode", R.string.GhostMode);
                String info = LocaleController.getString("GhostModeInfo", R.string.GhostModeInfo);
                textCell.setTextAndValueAndCheck(title, info, preferences.getBoolean("ghost", false), true, false);
            }
            if (position == noTypingRow) {
                String title = LocaleController.getString("NoTyping", R.string.NoTyping);
                String info = LocaleController.getString("NoTypingInfo", R.string.NoTypingInfo);
                textCell.setTextAndValueAndCheck(title, info, preferences.getBoolean("noTyping", false), true, false);
            }
            if (position == noReadingRow) {
                String title = LocaleController.getString("NoReading", R.string.NoReading);
                String info = LocaleController.getString("NoReadingInfo", R.string.NoReadingInfo);
                textCell.setTextAndValueAndCheck(title, info, preferences.getBoolean("noReading", false), true, false);
            }
            if (position == forceRearCamRow) {
                String title = LocaleController.getString("ForceRearCam", R.string.ForceRearCam);
                String info = LocaleController.getString("ForceRearCamDesc", R.string.ForceRearCamDesc);
                textCell.setTextAndValueAndCheck(title, info, preferences.getBoolean("forceRearCam", false), true, false);
            }
        }

        private void bindHeaderCell(RecyclerView.ViewHolder holder, int position) {
            HeaderCell headerCell = (HeaderCell) holder.itemView;
            if (position == sectionRow1) {
                headerCell.setText(LocaleController.getString("General", R.string.General));
            }
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            return position == ghostRow || position == noReadingRow || position == noTypingRow || position == forceRearCamRow;
        }
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCheckCell.class, HeaderCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        themeDescriptions.add(new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));

        themeDescriptions.addAll(getActionBarThemeDescriptions());
        themeDescriptions.addAll(getListViewThemeDescriptions());

        return themeDescriptions;
    }

    private ArrayList<ThemeDescription> getActionBarThemeDescriptions() {
        ArrayList<ThemeDescription> descriptions = new ArrayList<>();

        descriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_avatar_backgroundActionBarBlue));
        descriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_avatar_actionBarIconBlue));
        descriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        descriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_avatar_actionBarSelectorBlue));
        descriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground));
        descriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem));

        return descriptions;
    }

    private ArrayList<ThemeDescription> getListViewThemeDescriptions() {
        ArrayList<ThemeDescription> descriptions = new ArrayList<>();

        descriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_avatar_backgroundActionBarBlue));
        descriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        descriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));

        descriptions.addAll(getCellTextColors());

        return descriptions;
    }

    private ArrayList<ThemeDescription> getCellTextColors() {
        ArrayList<ThemeDescription> descriptions = new ArrayList<>();

        descriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        descriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        descriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack));
        descriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked));

        descriptions.add(new ThemeDescription(listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));

        return descriptions;
    }
}
