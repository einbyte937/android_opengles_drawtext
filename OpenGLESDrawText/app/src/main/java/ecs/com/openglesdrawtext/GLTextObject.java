package ecs.com.openglesdrawtext;

import java.util.ArrayList;

public class GLTextObject {

    //Members

    public String sID = "";

    public String sText;

    public float fPointSize = 0.0f;

    public float x;

    public float y;

    public float[] fColour;

    public ArrayList< GLSquare > aSquares;


    public GLTextObject( )
    {
        sText = "default";

        x = 0.0f;

        y = 0.0f;

        fColour = new float[] { 1f, 1f, 1f, 1f };

        aSquares = new ArrayList< GLSquare > ( );

        calcSquares( false );
    }

    public GLTextObject( String txt, float xcoord, float ycoord, float psize, float[] fcolour,
                         boolean isGradient )
    {
        sText = txt;

        x = xcoord;

        y = ycoord;

        fPointSize = psize;

        fColour = fcolour;

        aSquares = new ArrayList< GLSquare > ( );

        calcSquares( isGradient );
    }

	public boolean validate( )
	{
		if( sText.compareTo( "" ) == 0 ) return false;

		return true;
	}

    public void calcSquares( boolean isGradient )
    {
        int[] dCharDims = getCharBoundsPX( fPointSize );
        int dCharCount = 1;

        //Used if isGradient
        float fGradLevel = 1f;

        for ( char ch : sText.toCharArray( ) )
        {
            //Get the hex code of the character
            String sHexCharCode = String.format( "%06x", ( int ) ch );

            //Get the hex code of the start of the characters unicode block
            String[] sCharRangeBounds = whichRange( sHexCharCode );





            //Calculate the squares co-ordinates. We are x rows and x cols from begin code.
            float fCurSquareX = x + ( dCharDims[0] * dCharCount );

            float[] fCurSquareVecs = new float[] {
                    fCurSquareX, y - dCharDims[1], 0.0f,
                    fCurSquareX, y, 0.0f,
                    fCurSquareX + dCharDims[0], y, 0.0f,
                    fCurSquareX + dCharDims[0], y - dCharDims[1], 0.0f };

            float[] fCurSquareColours;

            if( isGradient )
            {
                fCurSquareColours = new float[]{
                        fColour[0], fColour[1], fGradLevel, fColour[3],
                        fColour[0], fColour[1], fGradLevel, fColour[3],
                        fColour[0], fColour[1], fGradLevel, fColour[3],
                        fColour[0], fColour[1], fGradLevel, fColour[3],
                };
            }
            else {

                fCurSquareColours = new float[]{
                        fColour[0], fColour[1], fColour[2], fColour[3],
                        fColour[0], fColour[1], fColour[2], fColour[3],
                        fColour[0], fColour[1], fColour[2], fColour[3],
                        fColour[0], fColour[1], fColour[2], fColour[3],
                };
            }





            //Calculate the texture co-ordinates. Let us start with 0's and then build
            int dRow = 1;
            int dCol = 1;
            int dCount = 1;

            int dBeginCode = Integer.parseInt( sCharRangeBounds[0], 16 );
            int dCurChar = Integer.parseInt( sHexCharCode, 16 );
            int dMapRows = Integer.parseInt( sCharRangeBounds[2] );

            float dCharWidth = (float) ( (double) 1 / (double) 50 );
            float dCharHeight = (float) ( (double) 1 / (double) dMapRows );

            while( dCurChar > dBeginCode )
            {
                //Try this way
                if( dCount % 50 == 0 )
                {
                    dRow++;
                    dCol = 0;
                }

                dBeginCode++;

                dCol++;

                dCount++;

            }

            float[] fCurTextCoords = new float[] {
                    (dCol - 1) * dCharWidth, (dRow - 1) * dCharHeight,
                    (dCol - 1) * dCharWidth, (dRow) * dCharHeight,
                    (dCol) * dCharWidth, (dRow) * dCharHeight,
                    (dCol) * dCharWidth, (dRow - 1) * dCharHeight
            };

            //Add a square to be drawn and textured with a character from one of the texture maps
            aSquares.add( new GLSquare( fCurSquareVecs, fCurSquareColours, fCurTextCoords ) );

            //Increments & Decrements
            dCharCount++;
            fGradLevel -= 0.2f;
            if( fGradLevel <= 0.2f ) fGradLevel = 1f;
        }
    }


    static public String[] whichRange( String sHexCharCode )
    {
        int dChar = Integer.parseInt( sHexCharCode, 16 );

        //new String[] { "STARTHEX","ENDHEX","NUMROWSINMAP","NAMEOFMAP" }
        //These are not all correct and you will need to modify them. There may be at some later date
        //a routine does does this calculation when the program starts. Right now I am only working in Latins
        if     ( dChar >= 0x0020  && dChar <= 0x007F ) { return new String[]{"0020","007F","2","LatinBasic.png"}; }
        else if( dChar >= 0x00A0  && dChar <= 0x00FF ) { return new String[]{"00A0","00FF","2","LatinSupplement.png"}; }
        else if( dChar >= 0x0100  && dChar <= 0x017F ) { return new String[]{"0100","017F","2","LatinExtendedA.png"}; }
        else if( dChar >= 0x0180  && dChar <= 0x024F ) { return new String[]{"0180","024F","2","LatinExtendedB.png"}; }
        else if( dChar >= 0x0250  && dChar <= 0x02AF ) { return new String[]{"0250","02AF","2","IPAEXtensions.png"}; }
        else if( dChar >= 0x02B0  && dChar <= 0x02FF ) { return new String[]{"02B0","02FF","2","Spacing.png"}; }
        else if( dChar >= 0x0300  && dChar <= 0x036F ) { return new String[]{"0300","036F","2","CombiningDiacriticalMarks.png"}; }
        else if( dChar >= 0x0370  && dChar <= 0x03FF ) { return new String[]{"0370","03FF","2","Greek_Coptic.png"}; }
        else if( dChar >= 0x0400  && dChar <= 0x04FF ) { return new String[]{"0400","04FF","2","Cyrillic.png"}; }
        else if( dChar >= 0x0500  && dChar <= 0x052F ) { return new String[]{"0500","052F","2","CyrillicSupplementary.png"}; }
        else if( dChar >= 0x0530  && dChar <= 0x058F ) { return new String[]{"0530","058F","2","Armenian.png"}; }
        else if( dChar >= 0x0590  && dChar <= 0x05FF ) { return new String[]{"0590","05FF","2","Hebrew.png"}; }
        else if( dChar >= 0x0600  && dChar <= 0x06FF ) { return new String[]{"0600","06FF","2","Arabic.png"}; }
        else if( dChar >= 0x0700  && dChar <= 0x074F ) { return new String[]{"0700","074F","2","Syriac.png"}; }
        else if( dChar >= 0x0780  && dChar <= 0x07BF ) { return new String[]{"0780","07BF","2","Thaana.png"}; }
        else if( dChar >= 0x0900  && dChar <= 0x097F ) { return new String[]{"0900","097F","2","Devanagari.png"}; }
        else if( dChar >= 0x0980  && dChar <= 0x09FF ) { return new String[]{"0980","09FF","2","Bengali.png"}; }
        else if( dChar >= 0x0A00  && dChar <= 0x0A7F ) { return new String[]{"0A00","0A7F","2","Gurmukhi.png"}; }
        else if( dChar >= 0x0A80  && dChar <= 0x0AFF ) { return new String[]{"0A80","0AFF","2","Gujarati.png"}; }
        else if( dChar >= 0x0B00  && dChar <= 0x0B7F ) { return new String[]{"0B00","0B7F","2","Oriya.png"}; }
        else if( dChar >= 0x0B80  && dChar <= 0x0BFF ) { return new String[]{"0B80","0BFF","2","Tamil.png"}; }
        else if( dChar >= 0x0C00  && dChar <= 0x0C7F ) { return new String[]{"0C00","0C7F","2","Telugu.png"}; }
        else if( dChar >= 0x0C80  && dChar <= 0x0CFF ) { return new String[]{"0C80","0CFF","2","Kannada.png"}; }
        else if( dChar >= 0x0D00  && dChar <= 0x0D7F ) { return new String[]{"0D00","0D7F","2","Malayalam.png"}; }
        else if( dChar >= 0x0D80  && dChar <= 0x0DFF ) { return new String[]{"0D80","0DFF","2","Sinhala.png"}; }
        else if( dChar >= 0x0E00  && dChar <= 0x0E7F ) { return new String[]{"0E00","0E7F","2","Thai.png"}; }
        else if( dChar >= 0x0E80  && dChar <= 0x0EFF ) { return new String[]{"0E80","0EFF","2","Malayalam.png"}; }
        else if( dChar >= 0x0F00  && dChar <= 0x0FFF ) { return new String[]{"0F00","0FFF","2","Lao.png"}; }
        else if( dChar >= 0x1000  && dChar <= 0x109F ) { return new String[]{"1000","109F","2","Myanmar.png"}; }
        else if( dChar >= 0x10A0  && dChar <= 0x10FF ) { return new String[]{"10A0","10FF","2","Georgian.png"}; }
        else if( dChar >= 0x1100  && dChar <= 0x11FF ) { return new String[]{"1100","11FF","2","HangulJamo.png"}; }
        else if( dChar >= 0x1200  && dChar <= 0x137F ) { return new String[]{"1200","137F","2","Ethiopic.png"}; }
        else if( dChar >= 0x13A0  && dChar <= 0x13FF ) { return new String[]{"13A0","13FF","2","Cherokee.png"}; }
        else if( dChar >= 0x1400  && dChar <= 0x167F ) { return new String[]{"1400","167F","2","CanadianAboriginal.png"}; }
        else if( dChar >= 0x1680  && dChar <= 0x169F ) { return new String[]{"1680","169F","2","Ogham.png"}; }
        else if( dChar >= 0x16A0  && dChar <= 0x16FF ) { return new String[]{"16A0","16FF","2","Runic.png"}; }
        else if( dChar >= 0x1700  && dChar <= 0x171F ) { return new String[]{"1700","171F","2","Tagalog.png"}; }
        else if( dChar >= 0x1720  && dChar <= 0x173F ) { return new String[]{"1720","173F","2","Hanunoo.png"}; }
        else if( dChar >= 0x1740  && dChar <= 0x175F ) { return new String[]{"1740","175F","2","Buhid.png"}; }
        else if( dChar >= 0x1760  && dChar <= 0x177F ) { return new String[]{"1760","177F","2","Tagbanwa.png"}; }
        else if( dChar >= 0x1780  && dChar <= 0x17FF ) { return new String[]{"1780","17FF","2","Khmer.png"}; }
        else if( dChar >= 0x1800  && dChar <= 0x18AF ) { return new String[]{"1800","18AF","2","Mongolian.png"}; }
        else if( dChar >= 0x1900  && dChar <= 0x194F ) { return new String[]{"1900","194F","2","Limbu.png"}; }
        else if( dChar >= 0x1950  && dChar <= 0x197F ) { return new String[]{"1950","197F","2","TaiLe.png"}; }
        else if( dChar >= 0x19E0  && dChar <= 0x19FF ) { return new String[]{"19E0","19FF","2","KhmerSymbols.png"}; }
        else if( dChar >= 0x1D00  && dChar <= 0x1D7F ) { return new String[]{"1D00","1D7F","2","PhoneticExtensions.png"}; }
        else if( dChar >= 0x1E00  && dChar <= 0x1EFF ) { return new String[]{"1E00","1EFF","2","LatinExtendedAdditional.png"}; }
        else if( dChar >= 0x1F00  && dChar <= 0x1FFF ) { return new String[]{"1F00","1FFF","2","GreekExtended.png"}; }
        else if( dChar >= 0x2000  && dChar <= 0x206F ) { return new String[]{"2000","206F","2","GeneralPunctuation.png"}; }
        else if( dChar >= 0x2070  && dChar <= 0x209F ) { return new String[]{"2070","209F","2","SuperscriptsandSubscripts.png"}; }
        else if( dChar >= 0x20A0  && dChar <= 0x20CF ) { return new String[]{"20A0","20CF","2","CurrencySymbols.png"}; }
        else if( dChar >= 0x20D0  && dChar <= 0x20FF ) { return new String[]{"20D0","20FF","2","CombiningDiacriticalMarksforSymbols.png"}; }
        else if( dChar >= 0x2100  && dChar <= 0x214F ) { return new String[]{"2100","214F","2","LetterlikeSymbols.png"}; }
        else if( dChar >= 0x2150  && dChar <= 0x218F ) { return new String[]{"2150","218F","2","NumberForms.png"}; }
        else if( dChar >= 0x2190  && dChar <= 0x21FF ) { return new String[]{"2190","21FF","2","Arrows.png"}; }
        else if( dChar >= 0x2200  && dChar <= 0x22FF ) { return new String[]{"2200","22FF","2","MathematicalOperators.png"}; }
        else if( dChar >= 0x2300  && dChar <= 0x23FF ) { return new String[]{"2300","23FF","2","MiscellaneousTechnical.png"}; }
        else if( dChar >= 0x2400  && dChar <= 0x243F ) { return new String[]{"2400","243F","2","ControlPictures.png"}; }
        else if( dChar >= 0x2440  && dChar <= 0x245F ) { return new String[]{"2440","245F","2","OpticalCharacterRecognition.png"}; }
        else if( dChar >= 0x2460  && dChar <= 0x24FF ) { return new String[]{"2460","24FF","2","EnclosedAlphanumerics.png"}; }
        else if( dChar >= 0x2500  && dChar <= 0x257F ) { return new String[]{"2500","257F","2","BoxDrawing.png"}; }
        else if( dChar >= 0x2580  && dChar <= 0x259F ) { return new String[]{"2580","259F","2","BlockElements.png"}; }
        else if( dChar >= 0x25A0  && dChar <= 0x25FF ) { return new String[]{"25A0","25FF","2","GeometricShapes.png"}; }
        else if( dChar >= 0x2600  && dChar <= 0x26FF ) { return new String[]{"2600","26FF","2","MiscellaneousSymbols.png"}; }
        else if( dChar >= 0x2700  && dChar <= 0x27BF ) { return new String[]{"2700","27BF","2","Dingbats.png"}; }
        else if( dChar >= 0x27C0  && dChar <= 0x27EF ) { return new String[]{"27C0","27EF","2","MiscellaneousMathematicalSymbols-A.png"}; }
        else if( dChar >= 0x27F0  && dChar <= 0x27FF ) { return new String[]{"27F0","27FF","2","SupplementalArrows-A.png"}; }
        else if( dChar >= 0x2800  && dChar <= 0x28FF ) { return new String[]{"2800","28FF","2","BraillePatterns.png"}; }
        else if( dChar >= 0x2900  && dChar <= 0x297F ) { return new String[]{"2900","297F","2","SupplementalArrows-B.png"}; }
        else if( dChar >= 0x2980  && dChar <= 0x29FF ) { return new String[]{"2980","29FF","2","MiscellaneousMathematicalSymbols-B.png"}; }
        else if( dChar >= 0x2A00  && dChar <= 0x2AFF ) { return new String[]{"2A00","2AFF","2","SupplementalMathematicalOperators.png"}; }
        else if( dChar >= 0x2B00  && dChar <= 0x2BFF ) { return new String[]{"2B00","2BFF","2","MiscellaneousSymbolsandArrows.png"}; }
        else if( dChar >= 0x2E80  && dChar <= 0x2EFF ) { return new String[]{"2E80","2EFF","2","CJKRadicalsSupplement.png"}; }
        else if( dChar >= 0x2F00  && dChar <= 0x2FDF ) { return new String[]{"2F00","2FDF","2","KangxiRadicals.png"}; }
        else if( dChar >= 0x2FF0  && dChar <= 0x2FFF ) { return new String[]{"2FF0","2FFF","2","IdeographicDescriptionCharacters.png"}; }
        else if( dChar >= 0x3000  && dChar <= 0x303F ) { return new String[]{"3000","303F","2","CJKSymbolsandPunctuation.png"}; }
        else if( dChar >= 0x3040  && dChar <= 0x309F ) { return new String[]{"3040","309F","2","Hiragana.png"}; }
        else if( dChar >= 0x30A0  && dChar <= 0x30FF ) { return new String[]{"30A0","30FF","2","Katakana.png"}; }
        else if( dChar >= 0x3100  && dChar <= 0x312F ) { return new String[]{"3100","312F","2","Bopomofo.png"}; }
        else if( dChar >= 0x3130  && dChar <= 0x318F ) { return new String[]{"3130","318F","2","Malayalam.png"}; }
        else if( dChar >= 0x3190  && dChar <= 0x319F ) { return new String[]{"3190","319F","2","HangulCompatibilityJamo.png"}; }
        else if( dChar >= 0x31A0  && dChar <= 0x31BF ) { return new String[]{"31A0","31BF","2","BopomofoExtended.png"}; }
        else if( dChar >= 0x31F0  && dChar <= 0x31FF ) { return new String[]{"31F0","31FF","2","KatakanaPhoneticExtensions.png"}; }
        else if( dChar >= 0x3200  && dChar <= 0x32FF ) { return new String[]{"3200","32FF","2","EnclosedCJKLettersandMonths.png"}; }
        else if( dChar >= 0x3300  && dChar <= 0x33FF ) { return new String[]{"3300","33FF","2","CJKCompatibility.png"}; }
        else if( dChar >= 0x3400  && dChar <= 0x4DBF ) { return new String[]{"3400","4DBF","2","CJKUnelse if(iedIdeographsExtensionA.png"}; }
        else if( dChar >= 0x4DC0  && dChar <= 0x4DFF ) { return new String[]{"4DC0","4DFF","2","YijingHexagramSymbols.png"}; }
        else if( dChar >= 0x4E00  && dChar <= 0x9FFF ) { return new String[]{"4E00","9FFF","2","CJKUnelse if(iedIdeographs.png"}; }
        else if( dChar >= 0xA000  && dChar <= 0xA48F ) { return new String[]{"A000","A48F","2","YiSyllables.png"}; }
        else if( dChar >= 0xA490  && dChar <= 0xA4CF ) { return new String[]{"A490","A4CF","2","YiRadicals.png"}; }
        else if( dChar >= 0xAC00  && dChar <= 0xD7AF ) { return new String[]{"AC00","D7AF","2","HangulSyllables.png"}; }
        else if( dChar >= 0xD800  && dChar <= 0xDB7F ) { return new String[]{"D800","DB7F","2","HighSurrogates.png"}; }
        else if( dChar >= 0xDB80  && dChar <= 0xDBFF ) { return new String[]{"DB80","DBFF","2","HighPrivateUseSurrogates.png"}; }
        else if( dChar >= 0xDC00  && dChar <= 0xDFFF ) { return new String[]{"DC00","DFFF","2","LowSurrogates.png"}; }
        else if( dChar >= 0xE000  && dChar <= 0xF8FF ) { return new String[]{"E000","F8FF","2","PrivateUseArea.png"}; }
        else if( dChar >= 0xF900  && dChar <= 0xFAFF ) { return new String[]{"F900","FAFF","2","CJKCompatibilityIdeographs.png"}; }
        else if( dChar >= 0xFB00  && dChar <= 0xFB4F ) { return new String[]{"FB00","FB4F","2","AlphabeticPresentationForms.png"}; }
        else if( dChar >= 0xFB50  && dChar <= 0xFDFF ) { return new String[]{"FB50","FDFF","2","ArabicPresentationForms-A.png"}; }
        else if( dChar >= 0xFE00  && dChar <= 0xFE0F ) { return new String[]{"FE00","FE0F","2","VariationSelectors.png"}; }
        else if( dChar >= 0xFE20  && dChar <= 0xFE2F ) { return new String[]{"FE20","FE2F","2","CombiningHalfMarks.png"}; }
        else if( dChar >= 0xFE30  && dChar <= 0xFE4F ) { return new String[]{"FE30","FE4F","2","CJKCompatibilityForms.png"}; }
        else if( dChar >= 0xFE50  && dChar <= 0xFE6F ) { return new String[]{"FE50","FE6F","2","SmallFormVariants.png"}; }
        else if( dChar >= 0xFE70  && dChar <= 0xFEFF ) { return new String[]{"FE70","FEFF","2","ArabicPresentationForms-B.png"}; }
        else if( dChar >= 0xFF00  && dChar <= 0xFFEF ) { return new String[]{"FF00","FFEF","2","HalfwidthandFullwidthForms.png"}; }
        else if( dChar >= 0xFFF0  && dChar <= 0xFFFF ) { return new String[]{"FFF0","FFFF","2","Specials.png"}; }
        else if( dChar >= 0x10000 && dChar<= 0x1007F ) { return new String[]{"10000","1007F","2","LinearBSyllabary.png"}; }
        else if( dChar >= 0x10080 && dChar<= 0x100FF ) { return new String[]{"10080","100FF","2","LinearBIdeograms.png"}; }
        else if( dChar >= 0x10100 && dChar<= 0x1013F ) { return new String[]{"10100","1013F","2","AegeanNumbers.png"}; }
        else if( dChar >= 0x10300 && dChar<= 0x1032F ) { return new String[]{"10300","1032F","2","OldItalic.png"}; }
        else if( dChar >= 0x10330 && dChar<= 0x1034F ) { return new String[]{"10330","1034F","2","Gothic.png"}; }
        else if( dChar >= 0x10380 && dChar<= 0x1039F ) { return new String[]{"10380","1039F","2","Ugaritic.png"}; }
        else if( dChar >= 0x10400 && dChar<= 0x1044F ) { return new String[]{"10400","1044F","2","Deseret.png"}; }
        else if( dChar >= 0x10450 && dChar<= 0x1047F ) { return new String[]{"10450","1047F","2","Shavian.png"}; }
        else if( dChar >= 0x10480 && dChar<= 0x104AF ) { return new String[]{"10480","104AF","2","Osmanya.png"}; }
        else if( dChar >= 0x10800 && dChar<= 0x1083F ) { return new String[]{"10800","1083F","2","CypriotSyllabary.png"}; }
        else if( dChar >= 0x1D000 && dChar<= 0x1D0FF ) { return new String[]{"1D000","1D0FF","2","ByzantineMusicalSymbols.png"}; }
        else if( dChar >= 0x1D100 && dChar<= 0x1D1FF ) { return new String[]{"1D100","1D1FF","2","MusicalSymbols.png"}; }
        else if( dChar >= 0x1D300 && dChar<= 0x1D35F ) { return new String[]{"1D300","1D35F","2","TaiXuanJingSymbols.png"}; }
        else if( dChar >= 0x1D400 && dChar<= 0x1D7FF ) { return new String[]{"1D400","1D7FF","2","MathematicalAlphanumericSymbols.png"}; }
        else if( dChar >= 0x20000 && dChar<= 0x2A6DF ) { return new String[]{"20000","2A6DF","2","CJKUnelse if(iedIdeographsExtensionB.png"}; }
        else if( dChar >= 0x2F800 && dChar<= 0x2FA1F ) { return new String[]{"2F800","2FA1F","2","CJKCompatibilityIdeographsSupplement.png"}; }
        else if( dChar >= 0xE0000 && dChar<= 0xE007F ) { return new String[]{"E0000","E007F","2","Tags.png"}; }
        
        return new String[]{"","",""};
    }

    static public int[] getCharBoundsPX( float p )
    {
        if( p == 10f ) { return new int[] { 15, 20 }; }

        else if( p == 11f ) { return new int[] { 0, 0 }; }

        else if( p == 12f ) { return new int[] { 0, 0 }; }

        else if( p == 13f ) { return new int[] { 0, 0 }; }

        else if( p == 14f ) { return new int[] { 0, 0 }; }

        else if( p == 15f ) { return new int[] { 0, 0 }; }

        else if( p == 20f ) { return new int[] { 30, 50 }; }

        else { return new int[] { 0, 0 }; }
    }
}
