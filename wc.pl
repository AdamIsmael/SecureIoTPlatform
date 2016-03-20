{
local @ARGV =  qw<Chapters/Introduction.tex Chapters/Background.tex Chapters/Design.tex Chapters/Implementation.tex Chapters/StrengthOfSecurity.tex Chapters/Results.tex Chapters/CriticalEvaluation.tex Chapters/Conclusion.tex>;
do 'texcount.pl';
}