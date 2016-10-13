package com.unimelb.feelinglucky.snapsheet.Database;

/**
 * Created by Xuhui Chen (yorkfine) on 14/10/2016.
 */

public class SnapSheetDataStoreUtils {
    public static String createTable(final String tableName, final String[] columns, final String[] types, final String... constraints) {
        if (tableName == null) {
            throw new NullPointerException("Name must not be null");
        }
        if (columns == null) {
            throw new NullPointerException("Columns must not be null");
        }
        if (types == null || columns.length != types.length) {
            throw new IllegalArgumentException("length of columns and types not match");
        }
        String newColumns[] = new String[columns.length];
        for (int i = 0; i < newColumns.length; i++) {
            newColumns[i] = String.format("%s %s", columns[i], types[i]);
        }
        final StringBuilder sb = new StringBuilder("CREATE ");
        sb.append("TABLE ");
        sb.append(tableName);
        sb.append(' ');
        if (newColumns != null && newColumns.length > 0) {
            sb.append('(');
            sb.append(joinArray(newColumns, ',', true));
            if (constraints != null && constraints.length > 0) {
                sb.append(", ");
                sb.append(joinArray(constraints, ',', true));
                sb.append(' ');
            }
            sb.append(')');
        }
        return sb.toString();
    }

    private static String joinArray(final String[] columns, final char token, final boolean includeSpace) {
        final StringBuilder sb = new StringBuilder();
        final int length = columns.length;
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(includeSpace ? token + " " : token);
            }
            sb.append(columns[i]);
        }
        return sb.toString();
    }
}
