package com.cldfire.forumnotifier.util;

import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XpathUtils {

    public HtmlForm checkXpathListForForm(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlForm":
                        return (HtmlForm) htmlElement;

                }
            }
        }
        return null;
    }

    public HtmlSubmitInput checkXpathListForSubmitButton(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlSubmitInput":
                        return (HtmlSubmitInput) htmlElement;

                }
            }
        }
        return null;
    }

    public HtmlTextInput checkXpathListForTextInput(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlTextInput":
                        return (HtmlTextInput) htmlElement;

                }
            }
        }
        return null;
    }

    public HtmlPasswordInput checkXpathListForPasswordInput(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlPasswordInput": {
                        return (HtmlPasswordInput) htmlElement;
                    }
                }
            }
        }
        return null;
    }

    public HtmlCheckBoxInput checkXpathListForCheckBoxInput(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlCheckBoxInput": {
                        return (HtmlCheckBoxInput) htmlElement;
                    }
                }
            }
        }
        return null;
    }


    public String checkXpathListForString(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlSpan": {
                        HtmlSpan element = (HtmlSpan) htmlElement;
                        if (!element.asText().equals("")) {
                            System.out.println("Was HtmlSpan, returning " + element.asText());
                            return element.asText();
                        }
                    }

                    case "HtmlStrong": {
                        try {
                            HtmlStrong element = (HtmlStrong) htmlElement;
                            if (!element.asText().equals("")) {
                                System.out.println("Was HtmlStrong, returning " + element.asText());
                                return element.asText();
                            }
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }
                    }

                    case "HtmlDefinitionDescription": {
                        try {
                            HtmlDefinitionDescription element = (HtmlDefinitionDescription) htmlElement;
                            if (!element.asText().equals("")) {
                                System.out.println("Was HtmlDefinitionDescription, returning " + element.asText());
                                return element.asText();
                            }
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }
                    }

                    case "HtmlHeader": {
                        try {
                            HtmlHeader element = (HtmlHeader) htmlElement;
                            if (!element.asText().equals("")) {
                                System.out.println("Was HtmlHeader, returning " + element.asText());
                                return element.asText();
                            }
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }
                    }

                    case "HtmlAnchor": {
                        try {
                            HtmlAnchor element = (HtmlAnchor) htmlElement;
                            if (!element.asText().equals("")) {
                                System.out.println("Was HtmlAnchor, returning " + element.asText());
                                return element.asText();
                            }
                        } catch (ClassCastException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        System.out.println("XpathUtils couldn't get anything, returning null ON PURPOSE");
        return null;
    }

    public String checkXpathListForHref(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlAnchor": {
                        HtmlAnchor element = (HtmlAnchor) htmlElement;
                        System.out.println("Account URL Href: " + element.asText());
                        if (element.getAttribute("href") != null || !element.getAttribute("href").equals("")) {
                            return element.getAttribute("href");
                        } else if (!element.asText().equals("")) {
                            return element.asText();
                        }
                    }
                }
            }
        }
        return null;
    }

    public HtmlImage checkXpathListForImage(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlImage":
                        return (HtmlImage) htmlElement;

                }
            }
        }
        return null;
    }

    /*
    Stuff for finding elements from forms
     */

    public HtmlSubmitInput getSubmitInputFromForm(final List<String> searchList, final HtmlForm form) {
        for (String v : searchList) {
            Object htmlElement = null;
            String result = "";
            Pattern p = Pattern.compile("'([^']*)'");
            Matcher m = p.matcher(v);

            while (m.find()) {
                result = m.group(1);
            }

            if (v.startsWith("name")) {
                htmlElement = form.getInputByName(result);
            } else if (v.startsWith("value")) {
                htmlElement = form.getInputByValue(result);
            }

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlSubmitInput":
                        return (HtmlSubmitInput) htmlElement;
                }
            }
        }
        return null;
    }

    public HtmlCheckBoxInput getCheckboxInputFromForm(final List<String> searchList, final HtmlForm form) {
        for (String v : searchList) {
            Object htmlElement = null;
            String result = "";
            Pattern p = Pattern.compile("'([^']*)'");
            Matcher m = p.matcher(v);

            while (m.find()) {
                result = m.group(1);
            }

            if (v.startsWith("name")) {
                htmlElement = form.getInputByName(result);
            } else if (v.startsWith("value")) {
                htmlElement = form.getInputByValue(result);
            }

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlCheckBoxInput":
                        return (HtmlCheckBoxInput) htmlElement;
                }
            }
        }
        return null;
    }

    public HtmlTextInput getTextInputFromForm(final List<String> searchList, final HtmlForm form) {
        for (String v : searchList) {
            Object htmlElement = null;
            String result = "";
            Pattern p = Pattern.compile("'([^']*)'");
            Matcher m = p.matcher(v);

            while (m.find()) {
                result = m.group(1);
            }

            if (v.startsWith("name")) {
                htmlElement = form.getInputByName(result);
            } else if (v.startsWith("value")) {
                htmlElement = form.getInputByValue(result);
            }

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlTextInput":
                        return (HtmlTextInput) htmlElement;
                }
            }
        }
        return null;
    }

    public HtmlPasswordInput getPasswordInputFromForm(final List<String> searchList, final HtmlForm form) {
        for (String v : searchList) {
            Object htmlElement = null;
            String result = "";
            Pattern p = Pattern.compile("'([^']*)'");
            Matcher m = p.matcher(v);

            while (m.find()) {
                result = m.group(1);
            }

            if (v.startsWith("name")) {
                htmlElement = form.getInputByName(result);
            } else if (v.startsWith("value")) {
                htmlElement = form.getInputByValue(result);
            }

            if (htmlElement != null) {
                switch (htmlElement.getClass().getSimpleName()) {
                    case "HtmlPasswordInput":
                        return (HtmlPasswordInput) htmlElement;
                }
            }
        }
        return null;
    }
}
