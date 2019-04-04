
import com.fizzed.rocker.RenderingException;
import com.fizzed.rocker.runtime.DefaultRockerTemplate;
import com.fizzed.rocker.runtime.PlainTextUnloadedClassLoader;

import java.io.IOException;
import java.util.ArrayList;

// import @ [1:1]

/*
 * Auto generated code to render template /files.rocker.html
 * Do not edit this file. Changes will eventually be overwritten by Rocker parser!
 */
@SuppressWarnings("unused")
public class files extends com.fizzed.rocker.runtime.DefaultRockerModel {

    static public com.fizzed.rocker.ContentType getContentType() {
        return com.fizzed.rocker.ContentType.HTML;
    }

    static public String getTemplateName() {
        return "files.rocker.html";
    }

    static public String getTemplatePackageName() {
        return "";
    }

    static public String getHeaderHash() {
        return "-1618097059";
    }

    static public String[] getArgumentNames() {
        return new String[]{"files"};
    }

    // argument @ [2:2]
    private ArrayList files;

    public files files(ArrayList files) {
        this.files = files;
        return this;
    }

    public ArrayList files() {
        return this.files;
    }

    static public files template(ArrayList files) {
        return new files()
                .files(files);
    }

    @Override
    protected DefaultRockerTemplate buildTemplate() throws RenderingException {
        // optimized for performance (via rocker.optimize flag; no auto reloading)
        return new Template(this);
    }

    static public class Template extends com.fizzed.rocker.runtime.DefaultRockerTemplate {

        // <a href=\"
        static private final byte[] PLAIN_TEXT_0_0;
        // \">
        static private final byte[] PLAIN_TEXT_1_0;
        // </a><br>\n
        static private final byte[] PLAIN_TEXT_2_0;

        static {
            PlainTextUnloadedClassLoader loader = PlainTextUnloadedClassLoader.tryLoad(files.class.getClassLoader(), files.class.getName() + "$PlainText", "UTF-8");
            PLAIN_TEXT_0_0 = loader.tryGet("PLAIN_TEXT_0_0");
            PLAIN_TEXT_1_0 = loader.tryGet("PLAIN_TEXT_1_0");
            PLAIN_TEXT_2_0 = loader.tryGet("PLAIN_TEXT_2_0");
        }

        // argument @ [2:2]
        protected final ArrayList files;

        public Template(files model) {
            super(model);
            __internal.setCharset("UTF-8");
            __internal.setContentType(getContentType());
            __internal.setTemplateName(getTemplateName());
            __internal.setTemplatePackageName(getTemplatePackageName());
            this.files = model.files();
        }

        @Override
        protected void __doRender() throws IOException, RenderingException {
            // ForBlockBegin @ [3:1]
            __internal.aboutToExecutePosInTemplate(3, 1);
            try {
                final com.fizzed.rocker.runtime.IterableForIterator<String> __forIterator0 = new com.fizzed.rocker.runtime.IterableForIterator<String>(files);
                while (__forIterator0.hasNext()) {
                    final String file = __forIterator0.next();
                    try {
                        // PlainText @ [3:29]
                        __internal.aboutToExecutePosInTemplate(3, 29);
                        __internal.writeValue(PLAIN_TEXT_0_0);
                        // ValueExpression @ [4:10]
                        __internal.aboutToExecutePosInTemplate(4, 10);
                        __internal.renderValue(file, false);
                        // PlainText @ [4:15]
                        __internal.aboutToExecutePosInTemplate(4, 15);
                        __internal.writeValue(PLAIN_TEXT_1_0);
                        // ValueExpression @ [4:17]
                        __internal.aboutToExecutePosInTemplate(4, 17);
                        __internal.renderValue(file, false);
                        // PlainText @ [4:22]
                        __internal.aboutToExecutePosInTemplate(4, 22);
                        __internal.writeValue(PLAIN_TEXT_2_0);
                        // ForBlockEnd @ [3:1]
                        __internal.aboutToExecutePosInTemplate(3, 1);
                    } catch (com.fizzed.rocker.runtime.ContinueException e) {
                        // support for continuing for loops
                    }
                } // for end @ [3:1]
            } catch (com.fizzed.rocker.runtime.BreakException e) {
                // support for breaking for loops
            }
        }
    }

    private static class PlainText {

        static private final String PLAIN_TEXT_0_0 = "<a href=\"";
        static private final String PLAIN_TEXT_1_0 = "\">";
        static private final String PLAIN_TEXT_2_0 = "</a><br>\n";

    }

}
