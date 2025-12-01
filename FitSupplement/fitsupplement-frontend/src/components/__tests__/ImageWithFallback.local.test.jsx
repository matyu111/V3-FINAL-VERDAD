import React from 'react';
import { render, screen } from '@testing-library/react';
import ImageWithFallback from '../ImageWithFallback';

describe('ImageWithFallback local src', () => {
  test('uses provided local src immediately', () => {
    render(<ImageWithFallback src={'/images/local-sample.jpg'} alt={'local-img'} fallback={'/images/product-placeholder.svg'} />);
    const img = screen.getByAltText('local-img');
    expect(img).toBeInTheDocument();
    expect(img.src).toMatch(/local-sample\.jpg$/);
  });
});
