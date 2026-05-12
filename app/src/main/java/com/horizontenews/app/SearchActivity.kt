<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#FFFFFF">

    <LinearLayout
        android:id="@+id/search_header"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="8dp"
        android:gravity="center_vertical">

        <EditText
            android:id="@+id/edit_search"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:hint="Pesquisar notícias..."
            android:background="@drawable/search_background"
            android:padding="12dp" />

        <ImageButton
            android:id="@+id/btn_do_search"
            android:layout_width="48dp"
            android:layout_height="48dp"
            android:src="@android:drawable/ic_menu_search"
            android:background="?attr/selectableItemBackgroundBorderless" />
    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/search_header"
        android:layout_marginTop="50dp"
        android:orientation="vertical"
        android:gravity="center">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Siga-nos"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginBottom="20dp"/>

        <GridLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:columnCount="4"
            android:alignmentMode="alignMargins">

            <ImageButton
                android:id="@+id/btn_instagram"
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:layout_margin="10dp"
                android:src="@drawable/ic_instagram" 
                android:background="?attr/selectableItemBackgroundBorderless" />
            
            </GridLayout>
    </LinearLayout>

</RelativeLayout>