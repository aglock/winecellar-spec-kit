Your task is to implement , following the design + development brief below. Implement thoroughly, in a step-by-step manner, and use built-in, standard Tailwind CSS design tokens instead of hardcoding values.

For colors and font families, use the defined values present in
@tailwind.config.js, e.g. 'bg-primary-500' etc. instead of the hardcoded primary/secondary values in the JSON brief. For one-off colors/grays etc. the JSON values are acceptable.

**Requirements**

- responsive (full width bg with centered content on larger screens)
- theme aware components with light and dark mode support (you can toggle with @ThemeSwitch.tsx; make sure to include that in the menu bar)
  - update @data/config/colors.js to match the colors in the design brief
  - *important* make sure to include light and dark mode colors by using Tailwind's dark mode classes (dark:)
  - all components must adapt to theme changes
- *do not use* magic strings, hex values, or px values. Replace all with Tailwind classes if possible.
- split reusable or complex parts of the UI into components so the code is maintainable and easy to understand.
- if any sample data is generated, place it in a separate file to keep the code clean.


**Assignment brief**

Create a premium wine cellar management application inspired by a modern dashboard layout. The interface should have a dark vertical navigation menu on the left, a refined content area on the right, a page title at the top, a search field, a prominent primary action for adding a wine, a row of filter chips, utility actions, and a central wine listing that can work as both grid cards and list view.

The visual tone should feel calm, elegant, structured, and collector-oriented rather than loud or overly commercial. Use mostly restrained neutrals and let wine-type accents carry meaning: red wines use deep burgundy tones, white wines use warm golden tones, sparkling wines use champagne-gold tones, and rosé wines use dusty pink tones. These accents should appear on badges, small highlights, and subtle card treatments rather than dominating the whole surface.

Each wine card should present a media area, wine name, secondary descriptor, quick action, and compact metadata such as vintage, region, drinking window, quantity, and cellar location. Keep hierarchy strong and spacing clean. Cards should feel tactile but not heavy.

Support both light and dark themes. In light mode, use soft white surfaces on a very light tinted outer background. In dark mode, use layered charcoal surfaces with muted contrast and softened accent colors. Maintain strong legibility in both themes.

The layout must adapt cleanly across small mobile screens up to large desktop displays. On small screens, collapse the sidebar into a mobile navigation pattern and stack controls vertically. On larger screens, keep the full sidebar visible and show the wine listing as a multi-column grid.

Token rules and usage:
- Use spacing tokens consistently for shell padding, section gaps, card padding, and control gaps.
- Use radius tokens consistently across shell, buttons, chips, inputs, and cards.
- Use elevation tokens sparingly and rely mainly on surface contrast and borders.
- Use text tokens for title, section heading, body, metadata, and button label hierarchy.
- Use semantic surface tokens for page background, shell, elevated card, muted panel, border, and divider.
- Use semantic accent tokens for each wine type: red, white, sparkling, rosé, with default, soft background, border, and text-on-soft variants.
- Use interaction tokens for hover, active, focus, and disabled states across all interactive controls.
- Keep accents meaningful and sparse; neutrals should carry most of the layout.

**Design specification**

{
  // UI handoff brief derived from the provided inspiration screenshot.
  // This is an interpretation for implementation, not a pixel-perfect reverse engineering file.
  "meta": {
    "artifact": "Wine cellar app - inventory/listing view inspiration brief",
    "screen_type": "Desktop web app dashboard / management view",
    "primary_use_case": "Browse, filter, search, and manage wines in a cellar",
    "source_inspiration_summary": {
      "layout_pattern": "Left sidebar navigation + top utility bar + central content area",
      "content_pattern": "Filter chips + toolbar controls + card/grid listing",
      "visual_style": "Minimal SaaS admin dashboard, rounded surfaces, soft contrast, premium but approachable"
    },
    "design_intent_for_wine_app": {
      "tone": "Refined, calm, premium, collector-friendly",
      "keywords": [
        "editorial",
        "organized",
        "clean",
        "soft contrast",
        "luxury-lite",
        "high legibility",
        "inventory-focused"
      ]
    }
  },

  "canvas": {
    "desktop_reference_resolution": {
      "width_px": 1504,
      "height_px": 1020
    },
    "outer_background": {
      "description": "Full-page soft tinted background outside the app shell",
      "light_mode": "very light dusty blush/lilac tint",
      "dark_mode": "deep desaturated plum/charcoal tint"
    },
    "app_shell": {
      "description": "Large rounded container centered on page, containing sidebar and main app content",
      "shape": "rounded rectangle",
      "corner_radius_px": 28,
      "shadow": {
        "light_mode": "very subtle elevation, diffuse shadow",
        "dark_mode": "subtle ambient shadow with faint edge highlight"
      },
      "light_mode_surface": "near-white",
      "dark_mode_surface": "near-black / deep charcoal"
    }
  },

  "layout": {
    "overall_structure": {
      "type": "Two-column application shell",
      "columns": [
        {
          "name": "sidebar",
          "width_behavior": "fixed"
        },
        {
          "name": "main_content",
          "width_behavior": "fluid"
        }
      ],
      "regions": [
        "left navigation rail",
        "top content header / toolbar",
        "main content listing area"
      ]
    },
    "sidebar": {
      "approx_width_px": 224,
      "position": "left",
      "full_height_inside_shell": true,
      "background": {
        "light_mode": "near-black",
        "dark_mode": "black with slightly raised inner borders"
      },
      "section_dividers": "thin horizontal separators with low-contrast strokes",
      "padding": {
        "top_px": 28,
        "bottom_px": 28,
        "x_px": 20
      }
    },
    "main_content": {
      "padding": {
        "top_px": 22,
        "bottom_px": 28,
        "left_px": 28,
        "right_px": 28
      },
      "header_height_px": 68,
      "content_flow": [
        "page title row",
        "filter chips + utility controls row",
        "grid of item cards"
      ]
    }
  },

  "responsive_breakpoints": {
    // Match Tailwind default breakpoints exactly.
    "tailwind_defaults": {
      "sm": "640px",
      "md": "768px",
      "lg": "1024px",
      "xl": "1280px",
      "2xl": "1536px"
    },
    "layout_behavior_by_breakpoint": {
      "base_under_640": {
        "shell": "App shell becomes edge-to-edge or near edge-to-edge with reduced outer margin",
        "sidebar": "Collapse to top app bar + drawer pattern",
        "header": "Stack title, search, primary action vertically",
        "toolbar": "Filter chips scroll horizontally; utility controls wrap",
        "listing": "Single-column cards",
        "card_density": "Comfortable spacing, full-width card layout",
        "recommended_columns": 1
      },
      "sm_640_plus": {
        "sidebar": "Still hidden/collapsed by default unless app prioritizes persistent navigation",
        "header": "Title row may become two-line with search/action aligned under title if needed",
        "listing": "1-2 columns depending on available width",
        "recommended_columns": 1
      },
      "md_768_plus": {
        "sidebar": "Drawer or slim icon rail optional",
        "toolbar": "Filters and controls can sit on two rows cleanly",
        "listing": "2-column grid possible",
        "recommended_columns": 2
      },
      "lg_1024_plus": {
        "sidebar": "Persistent full sidebar",
        "header": "Single-row title + search + CTA",
        "toolbar": "Single-row chips and controls where possible",
        "listing": "3-column grid fits naturally, matching inspiration",
        "recommended_columns": 3
      },
      "xl_1280_plus": {
        "sidebar": "Persistent full sidebar",
        "listing": "3-4 columns depending on card min width",
        "recommended_columns": 3
      },
      "2xl_1536_plus": {
        "sidebar": "Persistent full sidebar",
        "listing": "4 columns feasible without reducing card readability",
        "recommended_columns": 4
      }
    },
    "recommended_card_min_width_px": 280,
    "recommended_grid_gap_px": 16
  },

  "visual_hierarchy": {
    "level_1": "Page title and primary CTA",
    "level_2": "Filter chips and search",
    "level_3": "Card imagery + wine name",
    "level_4": "Wine metadata and cellar stats",
    "level_5": "Secondary actions such as share / overflow"
  },

  "regions": {
    "sidebar": {
      "logo_area": {
        "position": "top-left",
        "contents": [
          "brand mark",
          "brand wordmark"
        ],
        "height_px": 44,
        "spacing_bottom_px": 28
      },
      "profile_area": {
        "position": "below logo",
        "contents": [
          "small circular avatar",
          "user name",
          "small status / role line"
        ],
        "layout": "avatar on left, text stack on right",
        "padding_y_px": 16
      },
      "nav_groups": [
        {
          "name": "primary_navigation",
          "items": [
            "Home",
            "Products",
            "Analytics",
            "Transactions"
          ]
        },
        {
          "name": "secondary_navigation",
          "items": [
            "Wallet",
            "Purchases"
          ]
        },
        {
          "name": "tertiary_navigation",
          "items": [
            "Community",
            "Settings"
          ]
        }
      ],
      "wine_cellar_adaptation": {
        "recommended_items": [
          "Dashboard",
          "My Cellar",
          "Wines",
          "Regions",
          "Producers",
          "Drinking Window",
          "Analytics",
          "Wishlist",
          "Community / Notes",
          "Settings"
        ],
        "active_item_treatment": "Slightly stronger text/icon emphasis, optional subtle pill or background tint"
      }
    },

    "top_header": {
      "title": {
        "text_style": "medium-bold, large, left aligned",
        "inspiration_label": "Products",
        "wine_cellar_label_recommendation": "My Cellar or Wines"
      },
      "search": {
        "position": "top-right within main header row",
        "shape": "rounded pill / rounded rectangle",
        "icon": "leading search icon",
        "placeholder": "Search wines...",
        "behavior": "full text search across wine name, producer, region, vintage, grape"
      },
      "primary_action": {
        "position": "right of search",
        "style": "solid dark pill button",
        "icon": "leading plus",
        "label": "Add Wine",
        "importance": "highest action in header"
      }
    },

    "toolbar_row": {
      "left_side": {
        "filter_chips": [
          "All",
          "Ready to Drink",
          "Cellaring",
          "Consumed",
          "Wishlist"
        ],
        "behavior": "single-select or segmented control style; counts optional"
      },
      "right_side": {
        "utility_controls": [
          "Collection selector dropdown",
          "Filter button",
          "Sort button",
          "Export button",
          "View toggle"
        ],
        "view_toggle": [
          "list",
          "grid"
        ]
      }
    },

    "content_listing": {
      "current_inspiration_mode": "card grid",
      "alternate_mode": "tabular list",
      "grid_alignment": "left aligned with equal-width cards",
      "top_spacing_px": 24
    }
  },

  "components": {
    "search_input": {
      "shape": "rounded-xl to pill-like rectangle",
      "height_px": 40,
      "padding_x_px": 14,
      "icon_slot": "left",
      "text_size": "sm",
      "placeholder_color": {
        "light_mode": "muted gray",
        "dark_mode": "muted cool gray"
      },
      "background": {
        "light_mode": "light neutral gray",
        "dark_mode": "slightly lifted charcoal"
      },
      "border": {
        "light_mode": "minimal or none",
        "dark_mode": "1px low-contrast stroke"
      }
    },

    "primary_button": {
      "shape": "pill",
      "height_px": 40,
      "padding_x_px": 18,
      "content": [
        "plus icon",
        "label"
      ],
      "light_mode": {
        "background": "near-black",
        "text": "white"
      },
      "dark_mode": {
        "background": "soft white / warm light gray",
        "text": "near-black"
      },
      "hover": "slight brightness shift",
      "focus": "clear high-contrast ring"
    },

    "filter_chip": {
      "shape": "pill",
      "height_px": 32,
      "padding_x_px": 14,
      "selected_state": {
        "light_mode": "dark fill with white text",
        "dark_mode": "light fill with dark text"
      },
      "default_state": {
        "light_mode": "white or very light gray with subtle border",
        "dark_mode": "deep charcoal with subtle border"
      },
      "count_display": "Optional badge or inline count in parentheses"
    },

    "toolbar_button": {
      "shape": "rounded-lg",
      "height_px": 36,
      "padding_x_px": 12,
      "icon_position": "leading, except view toggle",
      "background": {
        "light_mode": "white",
        "dark_mode": "charcoal"
      },
      "border": {
        "light_mode": "soft neutral border",
        "dark_mode": "muted neutral border"
      }
    },

    "wine_card": {
      "structure": [
        "accent/status tag",
        "thumbnail or bottle/media area",
        "overflow menu button",
        "wine title",
        "wine subtitle / classification",
        "secondary action",
        "metadata rows"
      ],
      "shape": "rounded-2xl",
      "border": {
        "light_mode": "1px soft neutral border",
        "dark_mode": "1px muted charcoal border with inner contrast"
      },
      "background": {
        "light_mode": "white",
        "dark_mode": "dark elevated surface"
      },
      "shadow": {
        "light_mode": "small soft elevation",
        "dark_mode": "minimal shadow, more reliance on border contrast"
      },
      "padding_px": 12,
      "internal_gap_px": 10,
      "image_area": {
        "aspect_ratio": "approximately 16:9 in inspiration",
        "corner_radius_px": 14,
        "fit": "cover",
        "content_for_wine_app": [
          "bottle photo",
          "label close-up",
          "region illustration",
          "shelf photo"
        ]
      },
      "title": {
        "font_weight": 500,
        "size": "base",
        "line_clamp": 1
      },
      "subtitle": {
        "size": "sm",
        "color": "muted",
        "examples": [
          "Red Wine · Bordeaux Blend",
          "White Wine · Riesling",
          "Sparkling · Champagne",
          "Rosé · Provence"
        ]
      },
      "secondary_action": {
        "type": "small pill button",
        "position": "top-right area under media or aligned with title block",
        "recommended_label": "Details",
        "alternatives": [
          "Open",
          "View",
          "Drink",
          "Note"
        ]
      },
      "metadata_rows": {
        "layout": "label left, value right",
        "examples_for_wine_app": [
          "Vintage",
          "Country",
          "Region",
          "Drinking Window",
          "Qty",
          "Cellar Location",
          "Value"
        ]
      }
    },

    "status_tag": {
      "position": "top-left overlay on media",
      "shape": "small rounded rectangle / pill",
      "padding_x_px": 8,
      "height_px": 24,
      "text_size": "xs to sm",
      "semantic_types": [
        "Ready",
        "Cellaring",
        "Peak",
        "Past Peak",
        "Wishlist"
      ]
    },

    "overflow_button": {
      "shape": "circular",
      "size_px": 28,
      "position": "top-right overlay on media",
      "icon": "ellipsis",
      "background": {
        "light_mode": "white with slight opacity",
        "dark_mode": "black/charcoal with slight opacity"
      }
    }
  },

  "spacing_system": {
    "outer_shell_margin_px": {
      "desktop": 24,
      "tablet": 16,
      "mobile": 8
    },
    "section_gap_px": 16,
    "component_gap_px": 12,
    "tight_gap_px": 8,
    "card_grid_gap_px": 16,
    "sidebar_item_gap_px": 10,
    "recommended_vertical_rhythm_px": 4
  },

  "typography": {
    "style": "Neutral modern sans-serif, high legibility, medium warmth",
    "weights_used": {
      "regular": "body copy and metadata",
      "medium": "navigation, card titles, buttons",
      "semibold": "page titles, important figures"
    },
    "scale": {
      "page_title": {
        "size": "24-28px",
        "line_height": "1.2"
      },
      "section_title": {
        "size": "16-18px",
        "line_height": "1.3"
      },
      "body": {
        "size": "14px",
        "line_height": "1.45-1.6"
      },
      "meta": {
        "size": "12-13px",
        "line_height": "1.4"
      },
      "button": {
        "size": "13-14px",
        "line_height": "1"
      }
    },
    "case_rules": {
      "page_titles": "Title case",
      "nav_labels": "Title case or sentence case, but consistent",
      "metadata_labels": "Sentence case",
      "badge_labels": "Sentence case"
    }
  },

  "iconography": {
    "style": "Simple outlined icons with consistent stroke weight",
    "size_px": {
      "sidebar": 18,
      "toolbar": 16,
      "buttons": 14,
      "search": 16
    },
    "wine_cellar_specific_icons": [
      "wine bottle",
      "sparkles / celebration for sparkling wines",
      "map pin for region",
      "calendar for vintage/drinking window",
      "thermometer for serving temperature",
      "bookmark / note"
    ]
  },

  "color_strategy": {
    // Only rough palette, focusing on accents and complex media per request.
    "light_mode": {
      "neutrals": {
        "app_bg": "#EAD6E3", // rough dusty blush/lilac page surround
        "surface": "#FCFCFC",
        "surface_subtle": "#F3F3F4",
        "text_primary": "#121212",
        "text_secondary": "#6B6B73",
        "border": "#E7E7EA"
      },
      "sidebar": {
        "bg": "#0A0A0C",
        "text_primary": "#F5F5F6",
        "text_secondary": "#A7A7AD",
        "divider": "#24242A"
      }
    },
    "dark_mode": {
      "neutrals": {
        "app_bg": "#1A1319",
        "surface": "#111214",
        "surface_elevated": "#17181C",
        "surface_subtle": "#1E2025",
        "text_primary": "#F3F4F6",
        "text_secondary": "#A0A6B0",
        "border": "#2A2D33"
      },
      "sidebar": {
        "bg": "#09090B",
        "text_primary": "#F3F4F6",
        "text_secondary": "#9CA3AF",
        "divider": "#20232A"
      }
    },
    "brand_accent": {
      "description": "Brand accent can remain restrained; do not overload UI with color",
      "suggestion": "Use muted jewel tones if a cellar brand accent is needed"
    },
    "wine_type_accents": {
      "red": {
        "light_mode": {
          "accent": "#7B1E2B",
          "soft_bg": "#F7E8EB",
          "soft_border": "#E8C3CB",
          "text_on_soft": "#6A1723"
        },
        "dark_mode": {
          "accent": "#C65A6D",
          "soft_bg": "#2A161B",
          "soft_border": "#513038",
          "text_on_soft": "#F1C4CC"
        },
        "usage": [
          "status tag",
          "thin card top border",
          "small dot next to wine type",
          "hover highlight"
        ]
      },
      "white": {
        "light_mode": {
          "accent": "#B79B3D",
          "soft_bg": "#FBF5DF",
          "soft_border": "#E6D69A",
          "text_on_soft": "#7C6722"
        },
        "dark_mode": {
          "accent": "#D5B85A",
          "soft_bg": "#2D2817",
          "soft_border": "#5A4B27",
          "text_on_soft": "#F3E3AA"
        },
        "usage": [
          "type pill",
          "metadata accents",
          "filter state"
        ]
      },
      "sparkling": {
        "light_mode": {
          "accent": "#C9A227",
          "soft_bg": "#FCF6DE",
          "soft_border": "#ECD98C",
          "text_on_soft": "#866C16"
        },
        "dark_mode": {
          "accent": "#E0BE54",
          "soft_bg": "#2C2614",
          "soft_border": "#5F5227",
          "text_on_soft": "#F5E7AF"
        },
        "usage": [
          "special celebratory states",
          "premium highlights",
          "sparkling wine card ribbons"
        ]
      },
      "rose": {
        "light_mode": {
          "accent": "#C96F8D",
          "soft_bg": "#FBEAF0",
          "soft_border": "#ECC2D0",
          "text_on_soft": "#974C68"
        },
        "dark_mode": {
          "accent": "#E39AB1",
          "soft_bg": "#2A1920",
          "soft_border": "#5C3844",
          "text_on_soft": "#F5CDD9"
        },
        "usage": [
          "rosé wine cards",
          "subtle tag treatment",
          "wishlist or seasonal states if needed"
        ]
      }
    },
    "status_semantics": {
      "success_ready_to_drink": "Can use green-adjacent semantic, but keep softer than standard SaaS green",
      "warning_cellaring": "Gold/amber-based",
      "alert_past_peak": "Muted red",
      "info_wishlist": "Brand-neutral cool accent"
    }
  },

  "imagery_and_media": {
    "inspiration_behavior": {
      "cards_use_large_preview_images": true,
      "images_are_primary_visual_anchor": true
    },
    "wine_app_guidance": {
      "preferred_media_priority": [
        "bottle image",
        "label image",
        "wine-on-shelf photo",
        "producer/region visual fallback"
      ],
      "fallback_strategy": "If no bottle photo exists, render a clean placeholder card with wine-type color accent and monogram",
      "image_treatment": {
        "radius_px": 14,
        "overlay_badges_allowed": true,
        "avoid_heavy_gradients": true
      }
    }
  },

  "interaction_patterns": {
    "hover": {
      "cards": [
        "slight elevation increase",
        "border darkens slightly",
        "media scales up very subtly"
      ],
      "buttons": [
        "slight contrast shift",
        "clear affordance but restrained"
      ],
      "nav_items": [
        "text brightens",
        "icon brightens",
        "subtle background tint optional"
      ]
    },
    "active_selected": {
      "filters": "High contrast selected pill",
      "nav": "Persistent emphasis on current route",
      "view_toggle": "Selected icon/button has clear fill state"
    },
    "focus": {
      "requirement": "Visible keyboard focus ring on all interactive elements",
      "treatment": "2px ring with sufficient contrast in both themes"
    },
    "press": {
      "behavior": "Small downward scale or contrast compression"
    }
  },

  "accessibility_and_readability": {
    "contrast": {
      "requirement": "All text and controls must maintain strong contrast in both themes",
      "special_note": "Muted metadata must still be readable against white and charcoal surfaces"
    },
    "text_truncation": {
      "card_title": "Clamp to 1 line on dense grids, 2 lines allowed on mobile",
      "metadata_values": "Avoid truncation where inventory data matters"
    },
    "touch_targets": {
      "minimum_px": 40
    },
    "motion": {
      "guidance": "Keep animations subtle and fast; avoid decorative motion that distracts from inventory tasks"
    }
  },

  "wine_domain_adaptation": {
    "replace_product_concept_with_wine_entity": {
      "title_example": "Chateau Musar Rouge 2017",
      "subtitle_example": "Red Wine · Lebanon · Bekaa Valley",
      "card_rows_example": [
        {
          "label": "Vintage",
          "value": "2017"
        },
        {
          "label": "Window",
          "value": "2025-2035"
        },
        {
          "label": "Qty",
          "value": "6"
        },
        {
          "label": "Cellar",
          "value": "Rack B2"
        }
      ]
    },
    "recommended_filters": [
      "All",
      "Red",
      "White",
      "Sparkling",
      "Rosé",
      "Ready to Drink",
      "Cellaring"
    ],
    "recommended_sort_options": [
      "Vintage",
      "Drink by",
      "Producer",
      "Region",
      "Quantity",
      "Recently added"
    ],
    "recommended_bulk_actions": [
      "Export",
      "Move location",
      "Mark consumed",
      "Add tasting note"
    ]
  },

  "dark_mode_translation_rules": {
    "do_not_invert_naively": true,
    "principles": [
      "Use layered dark surfaces instead of pure black everywhere",
      "Keep sidebar slightly darker than content surface",
      "Preserve premium mood with reduced saturation",
      "Accents should become lighter and softer, not neon",
      "Images may need slightly darker overlays for badge legibility"
    ]
  },

  "implementation_notes_for_developer_handoff": {
    "must_preserve": [
      "Left navigation hierarchy",
      "Strong header with search and primary action",
      "Chip-based filtering",
      "Dense but readable card layout",
      "Premium neutral palette with wine-type accents"
    ],
    "can_adapt": [
      "Exact card dimensions",
      "Exact border radius by a few pixels",
      "Button labels",
      "Whether list and grid are separate routes or a toggle state"
    ],
    "avoid": [
      "Overusing saturated wine colors across full surfaces",
      "Making cards visually noisy",
      "Using harsh borders or heavy shadows",
      "Mixing too many accent systems beyond wine type semantics"
    ]
  },

  "deliverables_expected_from_ui_build": {
    "views": [
      "Desktop grid listing",
      "Desktop list listing",
      "Tablet adapted grid",
      "Mobile stacked list/card view"
    ],
    "themes": [
      "Light mode",
      "Dark mode"
    ],
    "component_states": [
      "default",
      "hover",
      "focus",
      "active",
      "disabled",
      "selected"
    ]
  },

  "developer_prompt": "See markdown code block below."
}