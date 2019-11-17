function hiddenstates=GetHiddenStates(seq)
 
% Input: sequence of observations 
% Output: sequence of hidden states

TRANS_EST_2 = [0.6773    0.3227;
               0.0842    0.9158];


EMIS_EST_2 = [0.7689    0.0061    0.1713    0.0537;
    0.0799    0.6646    0.1136    0.1420];

p_2 = [0.2, 0.8];

TRANS_HAT_2 = [0 p_2; zeros(size(TRANS_EST_2,1),1) TRANS_EST_2];
EMIS_HAT_2 = [zeros(1,size(EMIS_EST_2,2)); EMIS_EST_2];


TRANS_EST_4 = [0.6794    0.3206    0         0;
               0    0.5366    0.4634         0;
               0         0    0.3485    0.6516;
               0.1508         0         0    0.8492];


EMIS_EST_4 = [0.6884    0.0015    0.3002    0.0099;
              0    0.7205    0.0102    0.2694;
              0.2894    0.3731    0.3362    0.0023;
              0.0005    0.8440    0.0021    0.1534];
          
p_4 = [0.25, 0.20, 0.10, 0.45];
TRANS_HAT_4 = [0 p_4; zeros(size(TRANS_EST_4,1),1) TRANS_EST_4];
EMIS_HAT_4 = [zeros(1,size(EMIS_EST_4,2)); EMIS_EST_4];



[PSTATES_2, logpseq_2]=hmmdecode(seq,TRANS_HAT_2,EMIS_HAT_2);
[PSTATES_4, logpseq_4]=hmmdecode(seq,TRANS_HAT_4,EMIS_HAT_4);
BIC_2=-2*logpseq_2+12*(log(length(seq))-log(2*pi));
BIC_4=-2*logpseq_4+24*(log(length(seq))-log(2*pi));
if BIC_2<BIC_4 
    hiddenstates=hmmviterbi(seq, TRANS_HAT_2, EMIS_HAT_2);
else
    hiddenstates=hmmviterbi(seq, TRANS_HAT_4, EMIS_HAT_4);
end

