package img;

//==========================================================
/**
 ** @author John Alan McDonald
 **/

public final class

FileTypeFilter

  extends Object

  implements java.io.FilenameFilter {

  public static final String VERSION = "2004-09-21";

  //==========================================================
  // slots
  //==========================================================

  private final String
  _suffix;

  //==========================================================
  // java.io.FilenameFilter methods
  //==========================================================

  public final boolean
  accept (final java.io.File dir,
          final String name) {

    return name.toLowerCase().endsWith(_suffix); }

  //==========================================================
  // construction
  //==========================================================

  public FileTypeFilter (final String suffix) {
    super();
    _suffix = suffix.toLowerCase(); }

  //==========================================================
  } // end of class
  //==========================================================

