package com.app.abcdadmin.files;

public interface PickerManagerCallbacks {
    void PickerManagerOnUriReturned();

    void PickerManagerOnStartListener();

    void PickerManagerOnProgressUpdate(int progress);

    void PickerManagerOnCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason);
}
