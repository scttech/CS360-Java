package com.example.app_poll.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CsvSampleDataLoader {

    private static final String TAG = "CsvSampleDataLoader";

    private final Context appContext;
    private final AppDatabaseHelper dbHelper;

    public CsvSampleDataLoader(Context context, AppDatabaseHelper dbHelper) {
        this.appContext = context.getApplicationContext();
        this.dbHelper = dbHelper;
    }

    public void populateData() {
        if (!dbHelper.isEmpty()) return;

        Map<String, Long> classKeyToId = new HashMap<>();
        Map<String, Long> appNameToId = new HashMap<>();

        try {
            // 1) classes.csv: name,section,semester,year
            try (CSVReader r = openCsv("classes.csv", 1)) {
                String[] row;
                while ((row = r.readNext()) != null) {
                    if (row.length < 4) {
                        Log.w(TAG, "Skip classes row (need 4 cols): " + String.join("|", row));
                        continue;
                    }
                    String name = row[0].trim();
                    String section = row[1].trim();
                    String semester = row[2].trim();
                    String year = row[3].trim();

                    long id = dbHelper.insertClass(name, section, semester, year);
                    if (id != -1) {
                        classKeyToId.put(classKey(name, section, year), id);
                    } else {
                        Log.w(TAG, "Insert class failed: " + name);
                    }
                }
            }

            // 2) apps.csv: name,url,description
            try (CSVReader r = openCsv("apps.csv", 1)) {
                String[] row;
                while ((row = r.readNext()) != null) {
                    if (row.length < 1) {
                        Log.w(TAG, "Skip apps row (need 1+ cols)");
                        continue;
                    }
                    String name = row[0].trim();
                    String url = row.length > 1 ? row[1].trim() : "";
                    String desc = row.length > 2 ? row[2].trim() : "";

                    long id = dbHelper.insertApp(name, url, desc);
                    if (id != -1) {
                        appNameToId.put(name, id);
                    } else {
                        Log.w(TAG, "Insert app failed: " + name);
                    }
                }
            }

            // 3) votes.csv: app_name,class_name,section,semester,year
            try (CSVReader r = openCsv("votes.csv", 1)) {
                String[] row;
                while ((row = r.readNext()) != null) {
                    if (row.length < 4) {
                        Log.w(TAG, "Skip vote row (need 4 cols)");
                        continue;
                    }
                    String appName = row[0].trim();
                    String className = row[1].trim();
                    String section = row[2].trim();
                    String year = row[3].trim();

                    Long appId = appNameToId.get(appName);
                    Long classId = classKeyToId.get(classKey(className, section, year));
                    if (appId == null || classId == null) {
                        Log.w(TAG, "Skip vote (missing FK): app=" + appName + ", class=" + className);
                        continue;
                    }
                    dbHelper.insertVote(appId, classId);
                }
            }

            Log.i(TAG, "Sample data loaded from CSV assets.");
        } catch (Exception e) {
            Log.e(TAG, "CSV seeding failed.", e);
        }
    }

    private CSVReader openCsv(String assetName, int skipHeaderLines) throws Exception {
        AssetManager am = appContext.getAssets();
        InputStream is = am.open(assetName);
        InputStreamReader ir = new InputStreamReader(is, StandardCharsets.UTF_8);
        return new CSVReaderBuilder(ir)
                .withSkipLines(Math.max(0, skipHeaderLines))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(',')
                        .withIgnoreQuotations(false)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build())
                .build();
    }

    private String classKey(String name, String section, String year) {
        return (name + "|" + section + "|" + year).toLowerCase();
    }
}
