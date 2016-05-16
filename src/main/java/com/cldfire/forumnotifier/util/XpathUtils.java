package com.cldfire.forumnotifier.util;

import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;

public class XpathUtils {

    public HtmlForm checkXpathListForForm(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                switch (htmlElement.getClass().getTypeName()) {
                    case "com.gargoylesoftware.htmlunit.html.HtmlForm": {
                        return (HtmlForm) htmlElement;
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
                System.out.println(htmlElement.getClass().getTypeName());

                switch (htmlElement.getClass().getTypeName()) {
                    case "com.gargoylesoftware.htmlunit.html.HtmlSpan": {
                        HtmlSpan element = (HtmlSpan) htmlElement;
                        if (!element.asText().equals("")) {
                            System.out.println("Was HtmlSpan, returning " + element.asText());
                            return element.asText();
                        }
                    }

                    case "com.gargoylesoftware.htmlunit.html.HtmlStrong": {
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

                    case "com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription": {
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

                    case "com.gargoylesoftware.htmlunit.html.HtmlHeader": {
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

                    case "com.gargoylesoftware.htmlunit.html.HtmlAnchor": {
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
                System.out.println(htmlElement.getClass().getTypeName());

                switch (htmlElement.getClass().getTypeName()) {
                    case "com.gargoylesoftware.htmlunit.html.HtmlAnchor": {
                        HtmlAnchor element = (HtmlAnchor) htmlElement;
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
                switch (htmlElement.getClass().getTypeName()) {
                    case "com.gargoylesoftware.htmlunit.html.HtmlImage": {
                        return (HtmlImage) htmlElement;
                    }
                }
            }
        }
        return null;
    }
}
