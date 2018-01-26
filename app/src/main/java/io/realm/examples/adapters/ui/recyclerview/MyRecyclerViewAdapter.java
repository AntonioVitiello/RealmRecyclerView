/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm.examples.adapters.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.examples.adapters.R;
import io.realm.examples.adapters.model.Counter;

class MyRecyclerViewAdapter extends RealmRecyclerViewAdapter<Counter, MyRecyclerViewAdapter.MyViewHolder> {

    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<Integer>();

    MyRecyclerViewAdapter(OrderedRealmCollection<Counter> data) {
        super(data, true);
        setHasStableIds(true);
    }

    void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
        if (!enabled) {
            countersToDelete.clear();
        }
        notifyDataSetChanged();
    }

    Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parentView, int viewType) {
        View itemView = LayoutInflater.from(parentView.getContext())
                .inflate(R.layout.row, parentView, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Counter counter = getItem(position);
        holder.data = counter;
        //noinspection ConstantConditions
        holder.title.setText(counter.toString());
        holder.deleteCheckBox.setChecked(countersToDelete.contains(counter.getCount()));
        if (inDeletionMode) {
            holder.deleteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        countersToDelete.add(counter.getCount());
                    } else {
                        countersToDelete.remove(counter.getCount());
                    }
                }
            });
        } else {
            holder.deleteCheckBox.setOnCheckedChangeListener(null);
        }
        holder.deleteCheckBox.setVisibility(inDeletionMode ? View.VISIBLE : View.GONE);
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getCount();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CheckBox deleteCheckBox;
        public Counter data;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textview);
            deleteCheckBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}
