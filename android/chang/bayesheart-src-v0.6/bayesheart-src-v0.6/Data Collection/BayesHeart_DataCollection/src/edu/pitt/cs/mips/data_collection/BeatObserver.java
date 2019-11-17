package edu.pitt.cs.mips.data_collection;


public interface BeatObserver
{
    public abstract void onBeat(int heartrate, int duration);
    public abstract void onCameraError(Exception exception, android.hardware.Camera.Parameters parameters);
    public abstract void onHBError();
    public abstract void onHBStart();
    public abstract void onHBStop();
    public abstract void onSample(long timestamp, float value);
    public abstract void onValidRR(long timestamp, int value);
    public abstract void onValidatedRR(long timestamp, int value);
}
