package com.example.transportpro;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> transportData = new MutableLiveData<>();
    private final MutableLiveData<String> sensitiveItem = new MutableLiveData<>();
    private final MutableLiveData<String> textData = new MutableLiveData<>();

    // Create a LiveData for receiver data using a data class
    private final MutableLiveData<ReceiverData> receiverData = new MutableLiveData<>();

    public void setReceiverData(ReceiverData data) {
        receiverData.setValue(data);
    }

    public LiveData<ReceiverData> getReceiverData() {
        return receiverData;
    }

    public void setTransportData(String data) {
        transportData.setValue(data);
    }

    public LiveData<String> getTransportData() {
        return transportData;
    }

    public void setSensitiveItem(String data) {
        sensitiveItem.setValue(data);
    }

    public LiveData<String> getSensitiveItem() {
        return sensitiveItem;
    }

//    public void setTextData(String data) {
//        textData.setValue(data);
//    }
//
//    public LiveData<String> getTextData() {
//        return textData;
//    }
}
