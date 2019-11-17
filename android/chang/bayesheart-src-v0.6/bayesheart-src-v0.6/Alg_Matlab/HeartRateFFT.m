function   hrs=HeartRateFFT(sig)
 
% Input: sequence of sample points
% Output: sequence of heart rate estimations

Fs = 20;                    % Sampling frequency
window=7;                   % Window size
overlap=2;                  % Window overlap
hrs=[];                     % Sequence of heart rate values
times=floor(length(sig)/Fs/overlap);
for i=1:times-window
    beginIndex=(i-1)*overlap*Fs+1;
    endIndex=beginIndex+window*Fs-1;
    out=sig(beginIndex:endIndex,1);

    T = 1/Fs;                   % Sample time
    L = length(out);            % Length of signal
    y=out;
    NFFT = 2^nextpow2(L);
    Y=fft(y,NFFT)/L;
    temp_y=2*abs(Y(1:NFFT/2+1));
    length_y=size(temp_y,1);
    cut_factor=0.075;
    temp_y(1:ceil(cut_factor*length_y))=0;
    [~,loc]=max(temp_y);
    loc=loc/length_y*10;
    f=60*loc;
    hrs=[hrs; f];
end

end

