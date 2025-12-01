// Setup file for Vitest + Testing Library
// Ensure Vitest's `expect` is available globally before importing jest-dom
// Use dynamic imports with top-level await so we control load order
// Import expect from vitest, attach it to globalThis, and only then load jest-dom.
const { expect: vitestExpect } = await import('vitest')
globalThis.expect = vitestExpect

// Now safely import jest-dom which calls expect.extend at module initialization time
await import('@testing-library/jest-dom')
