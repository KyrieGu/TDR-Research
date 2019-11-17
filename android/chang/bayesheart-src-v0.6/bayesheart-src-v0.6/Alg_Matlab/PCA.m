function selectedSig = NewPCA(rgb)

%Input: each row of matrix rgb is one observed signal (i.e., r,g,b signals)
%Output: selectedSig which is the most periodic component derived

testR=rgb(1,:);
testG=rgb(2,:);
testB=rgb(3,:);

meanR=mean(testR);
meanG=mean(testG);
meanB=mean(testB);
newR=(testR-meanR);
newG=(testG-meanG);
newB=(testB-meanB);

testrgb=[newR; newG; newB];
[E,D]=pcamat(testrgb)
result=E'*rgb;
result=result';

maxRatio=0;
numberofsignals=size(result(1,:));
  for index=1:numberofsignals
      sig=result(:,index);
      Fs = 20;                          
      L = length(sig);                   
      y=sig;
      NFFT = 2^nextpow2(L);
      Y=fft(y,NFFT)/L;
      temp_y=2*abs(Y(1:NFFT/2+1));
      length_y=size(temp_y,1);
      cut_factor=0.075;
      temp_y(1:ceil(cut_factor*length_y))=0;
      [~,loc]=max(temp_y);
      max_p=temp_y(loc);
      max_pp=temp_y(2*loc);
      max_pp=max_pp^2;
      max_p=max_p^2+max_pp;

    all_p=sum(temp_y.^2);
    ratio=max_p/all_p;
 if ratio>maxRatio
     maxRatio=ratio;
     selectedSig=sig;
 else
 end   
end
  selectedSig=selectedSig';
  if selectedSig(1)>0
      selectedSig=selectedSig*(-1);
  else
  end

