import React from 'react';

const ImageWithFallback = ({ src, alt, className, style, fallback = '/images/product-placeholder.svg', loading = 'lazy', ...rest }) => {
  // Accept fallback as either string (path) or function that returns a path
  const getFallbackPath = () => {
    try {
      return typeof fallback === 'function' ? fallback() : fallback;
    } catch (err) {
      return '/images/product-placeholder.svg';
    }
  };

  // Normalize public base (Vite serves `public/` at base URL)
  const publicBase = (typeof import.meta !== 'undefined' && import.meta.env && import.meta.env.BASE_URL) ? import.meta.env.BASE_URL : '/';

  const fallbackPath = getFallbackPath() || `${publicBase}images/product-placeholder.svg`;

  // Show the category/local fallback immediately so cards always display an image.
  const [displaySrc, setDisplaySrc] = React.useState(() => {
    try {
      const fb = getFallbackPath();
      return fb || fallbackPath || src || `${publicBase}images/product-placeholder.svg`;
    } catch (err) {
      return src || `${publicBase}images/product-placeholder.svg`;
    }
  });

  React.useEffect(() => {
    let mounted = true;

    // If no src provided, nothing to preload — keep the fallback displayed
    if (!src) {
      return () => { mounted = false; };
    }

    // If src looks like a local/bundled asset or already equals the fallback, no need to preload
    try {
      const lower = String(src).toLowerCase();
      if (lower.includes('product-placeholder') || lower.startsWith(publicBase) || lower.startsWith('/') || lower.startsWith('data:') || lower.startsWith('blob:')) {
        // already local or placeholder — ensure it's set
        if (mounted) setDisplaySrc(src);
        return () => { mounted = false; };
      }
    } catch (err) {}

    // Preload remote image: if it loads, swap from the fallback to the remote image.
    const loader = new Image();
    loader.onload = () => {
      if (!mounted) return;
      if (import.meta && import.meta.env && import.meta.env.MODE !== 'production') {
        // eslint-disable-next-line no-console
        console.debug('[ImageWithFallback] loaded remote', src, '-> using remote image');
      }
      setDisplaySrc(src);
    };
    loader.onerror = () => {
      if (!mounted) return;
      if (import.meta && import.meta.env && import.meta.env.MODE !== 'production') {
        // eslint-disable-next-line no-console
        console.debug('[ImageWithFallback] failed to load remote', src, '-> using category/local fallback');
      }
      try {
        const fb = getFallbackPath();
        if (mounted) setDisplaySrc(fb || fallbackPath);
      } catch (err) {
        if (mounted) setDisplaySrc(fallbackPath);
      }
    };
    // Start loading (use absolute URL as provided)
    loader.src = src;

    return () => {
      mounted = false;
      loader.onload = null;
      loader.onerror = null;
    };
  }, [src, fallbackPath, publicBase]);

  const handleError = (e) => {
    try {
      const img = e?.currentTarget;
      if (!img) return;
      // If image element failed to render current displaySrc, switch to fallback path
      const cur = img.src || '';
      if (cur.includes('product-placeholder') || cur.includes('placeholder')) return;
      img.onerror = null;
      try {
        const fb = getFallbackPath();
        img.src = fb || fallbackPath;
        setDisplaySrc(fb || fallbackPath);
      } catch (err) {
        img.src = fallbackPath;
        setDisplaySrc(fallbackPath);
      }
    } catch (err) {
      try { e.currentTarget.src = `${publicBase}images/product-placeholder.svg`; } catch {}
    }
  };

  return (
    <img
      src={displaySrc}
      alt={alt || 'Imagen del producto'}
      className={className}
      style={style}
      loading={loading}
      onError={handleError}
      {...rest}
    />
  );
};

export default ImageWithFallback;
