<?xml version="1.0" encoding="utf-8"?>

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="#FFFFFF">

    <View
        android:layout_width="match_parent"
        android:layout_height="4dp"
        android:background="#F29121" />

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:background="#FFFFFF"
        android:elevation="2dp"
        app:contentInsetStart="0dp">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_centerInParent="true"
                android:text="HORIZONTE NEWS"
                android:textColor="#000000"
                android:textSize="15sp"
                android:textStyle="bold"
                android:letterSpacing="0.1" />

            <ImageButton
                android:id="@+id/btn_open_search"
                android:layout_width="48dp"
                android:layout_height="48dp"
                android:layout_alignParentEnd="true"
                android:layout_centerVertical="true"
                android:layout_marginEnd="8dp"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:src="@drawable/ic_search_black"
                android:contentDescription="Pesquisar"
                app:tint="#000000" />

        </RelativeLayout>

    </androidx.appcompat.widget.Toolbar>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#EEEEEE" />

    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        android:id="@+id/swipeRefreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:paddingTop="8dp"
            android:clipToPadding="false" />

    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>

</LinearLayout>