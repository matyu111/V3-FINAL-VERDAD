import React from 'react';
import { render, screen } from '@testing-library/react';
import ImageWithFallback from '../ImageWithFallback';

describe('ImageWithFallback component', () => {
  test('renders img with fallback when no src provided', () => {
    render(<ImageWithFallback src={null} fallback={'/images/product-placeholder.svg'} alt="mi-imagen" />);
    const img = screen.getByAltText('mi-imagen');
    expect(img).toBeInTheDocument();
    // src in jsdom becomes an absolute URL; assert the filename is present
    expect(img.src).toMatch(/product-placeholder\.svg$/);
  });
});
