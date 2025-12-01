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

  const fallbackPath = getFallbackPath() || '/images/product-placeholder.svg';

  const handleError = (e) => {
    // If the image failed, set to fallback (avoid replacing again if already placeholder)
    if (e?.currentTarget?.src && !e.currentTarget.src.includes('product-placeholder')) {
      e.currentTarget.src = fallbackPath;
      e.currentTarget.onerror = null;
    }
  };

  return (
    <img
      src={src || fallbackPath}
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
